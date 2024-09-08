package ir.jibit.data_access.db;

import java.sql.SQLException;

/**
 * This is postgres implementation of <i>Executable</i>
 * Methods have default database implementations in <i>DBExecutable</i>.
 * Methods should be overridden here in case of requiring different implementation.
 *
 * @author mrahimian
 */
public class PostgresDBExecutable extends DBExecutable {
    public PostgresDBExecutable() throws SQLException {
    }
}
