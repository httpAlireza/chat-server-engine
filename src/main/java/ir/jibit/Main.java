package ir.jibit;

import io.vertx.core.Vertx;
import ir.jibit.data_access.db.DataSource;
import ir.jibit.presentation.ProtocolFactory;

import java.sql.SQLException;

public class Main {
    public final static ConfigGetter configGetter = new DefaultConfigGetter();

    public static void main(String[] args) throws SQLException {

        /*
        getting a connection from pool and closing it for testing if the app is connected to database correctly.
        if there is a problem in connecting to database app throws an SQLException.
        */
        var x = DataSource.getConnection();
        x.close();

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ProtocolFactory.getProtocol(configGetter.getProtocol(), configGetter.getPort()));
    }
}
