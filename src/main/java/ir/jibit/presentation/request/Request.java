package ir.jibit.presentation.request;

import io.vertx.ext.web.RoutingContext;

/**
 * This abstract class is meant to turn network requests to java objects. its subclasses can have additional fields
 * based on their decoded request body.
 * All subclasses have to implement logMessage method by their own for logging purposes.
 *
 * @author Alireza khodadoust
 */
public abstract class Request {
    private final String method;

    private long execution_start_time;

    private RoutingContext ctx;

    public Request(String method) {
        this.method = method;
        execution_start_time = System.currentTimeMillis();
    }

    public String getMethod() {
        return method;
    }

    public abstract String logMessage();

    public long getExecution_start_time() {
        return execution_start_time;
    }

    public RoutingContext getCtx() {
        return ctx;
    }

    public void setCtx(RoutingContext ctx) {
        this.ctx = ctx;
    }
}
