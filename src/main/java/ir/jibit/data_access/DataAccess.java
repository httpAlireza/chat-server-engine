package ir.jibit.data_access;

import ir.jibit.data_access.db.PostgresDBExecutable;
import ir.jibit.presentation.request.GetMessagesRequest;
import ir.jibit.presentation.request.NotFoundRequest;
import ir.jibit.presentation.request.Request;
import ir.jibit.presentation.request.SendTextMessageRequest;

import java.sql.SQLException;

import static ir.jibit.Main.configGetter;

public class DataAccess {
    private final Executable executable;


    public DataAccess() {
        try {
            if (configGetter.getResource().equals(Resource.POSTGRESQL)) {
                executable = new PostgresDBExecutable();
            } else {
                // TODO: 10/30/22 throw suitable exception
                executable = null;
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public void run(Request req) {
        if (req instanceof SendTextMessageRequest) {
            executable.execute((SendTextMessageRequest) req);
        } else if (req instanceof GetMessagesRequest) {
            executable.execute((GetMessagesRequest) req);
        } else if (req instanceof NotFoundRequest) {
            executable.execute((NotFoundRequest) req);
        }
    }
}
