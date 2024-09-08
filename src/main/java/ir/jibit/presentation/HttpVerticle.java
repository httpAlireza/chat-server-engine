package ir.jibit.presentation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import ir.jibit.Chat;
import ir.jibit.business_logic.Dispatcher;
import ir.jibit.data_access.ResponseQueue;
import ir.jibit.presentation.request.GetMessagesRequest;
import ir.jibit.presentation.request.IntermediaryGetMessageRequest;
import ir.jibit.presentation.request.NotFoundRequest;
import ir.jibit.presentation.request.SendTextMessageRequest;
import ir.jibit.presentation.response.StatusCode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ir.jibit.Main.configGetter;

public class HttpVerticle extends AbstractVerticle {

    private final static Gson gson = new Gson();
    private final int port;
    private final Dispatcher dispatcher;

    private final ExecutorService es;

    private final Runnable responseConsumer;


    public HttpVerticle(int port) {
        this.port = port;
        dispatcher = new Dispatcher();
        es = Executors.newFixedThreadPool(configGetter.responseConsumerThreadNumber());
        responseConsumer = () -> {
            try {
                var response = ResponseQueue.getInstance().popElement();
                var responseBody = gson.toJson(response.getMessage());
                response.getRequest().getCtx().response().setStatusCode(response.getStatusCode().getCode())
                        .setStatusMessage(response.getStatusCode().getStatusMessage())
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .putHeader("content-length", String.valueOf(responseBody.length()))
                        .end(responseBody);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

    }

    @Override
    public void start(Promise<Void> startPromise) {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.route().handler(ctx -> {
            es.submit(responseConsumer);
            ctx.next();
        });

        //GetMessagesRequest
        router.route(HttpMethod.POST, "/user").handler(ctx -> {
            HttpServerRequest request = ctx.request();
            String method = request.method().toString().toUpperCase();
            String body = ctx.body().asString();
            var req = createGetMessagesRequest(method, body);
            req.setCtx(ctx);
            dispatcher.handle(req);
        });

        //SensTextMessageRequest
        router.route(HttpMethod.POST, "/user/:userId").handler(ctx -> {
            HttpServerRequest request = ctx.request();
            String method = request.method().toString().toUpperCase();
            String body = ctx.body().asString();
            try {
                long fromId = Long.parseLong(request.getParam("userId"));
                var req = createSendTextMessageRequest(method, body, fromId);
                req.setCtx(ctx);
                dispatcher.handle(req);
            } catch (NumberFormatException e) {
                var req = createNotFoundRequest(method);
                req.setCtx(ctx);
                dispatcher.handle(req);
            }

        });

        //NotFoundRequest
        router.errorHandler(StatusCode.NOT_FOUND.getCode(), ctx -> {
            HttpServerRequest request = ctx.request();
            String method = request.method().toString().toUpperCase();
            var req = createNotFoundRequest(method);
            req.setCtx(ctx);
            dispatcher.handle(req);
        });

        server.requestHandler(router).listen(port, http -> {
            if (http.succeeded()) {
                startPromise.complete();
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

    /**
     * This method sets parameters of related <i>GetMessagesRequest</i> according to incoming request.
     *
     * @return The related <i>GetMessagesRequest</i>
     */
    private GetMessagesRequest createGetMessagesRequest(String method, String body) {
        var temp = gson.fromJson(body, IntermediaryGetMessageRequest.class);
        return new GetMessagesRequest(method, temp);
    }

    /**
     * This method sets parameters of related <i>SendTextMessageRequest</i> according to incoming request.
     *
     * @return The related <i>SendTextMessageRequest</i>
     */
    private SendTextMessageRequest createSendTextMessageRequest(String method, String body, long fromId) {
        var chat = (Chat) gson.fromJson(body, new TypeToken<Chat<String>>() {
        }.getType());
        chat.setFrom(fromId);
        return new SendTextMessageRequest(method, chat);
    }

    private NotFoundRequest createNotFoundRequest(String method) {
        return new NotFoundRequest(method);
    }
}
