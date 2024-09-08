package ir.jibit.data_access.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static ir.jibit.Main.configGetter;

/**
 * Set database configurations with hikaricp and return <i>Connection</i>.
 *
 * @author mrahimian
 */
public class DataSource {

    private final static HikariConfig config = new HikariConfig("src/main/resources/hikari.properties");

    static {
        config.setMaximumPoolSize(configGetter.dbConnectionNumber());
    }

    private final static HikariDataSource ds = new HikariDataSource(config);

    private DataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
