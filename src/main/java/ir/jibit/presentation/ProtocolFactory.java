package ir.jibit.presentation;

import io.vertx.core.Verticle;

public class ProtocolFactory {
    public static Verticle getProtocol(Protocol protocol, int port) {
        return switch (protocol) {
            case HTTP -> new HttpVerticle(port);
        };
    }
}
