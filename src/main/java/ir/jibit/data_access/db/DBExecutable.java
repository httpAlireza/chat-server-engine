package ir.jibit.data_access.db;

import ir.jibit.data_access.Executable;
import ir.jibit.presentation.request.GetMessagesRequest;
import ir.jibit.presentation.request.SendTextMessageRequest;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ir.jibit.Main.configGetter;

/**
 * This is the default database implementation of Executable
 * If a database instance has another implementation, It should override related methods.
 */
public abstract class DBExecutable implements Executable {

    private final DBInteract insertHandler;
    private final DBInteract fetchHandler;
    private final ScheduledExecutorService insertScheduler = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService fetchScheduler = Executors.newSingleThreadScheduledExecutor();


    public DBExecutable() {
        insertHandler = new InsertMessageHandler(new ConcurrentLinkedQueue<>());
        insertScheduler.scheduleAtFixedRate(insertHandler, 1000, configGetter.batchInsertTimeThreshold(), TimeUnit.MILLISECONDS);

        fetchHandler = new FetchMessagesHandler(new ConcurrentLinkedQueue<>());
        fetchScheduler.scheduleAtFixedRate(fetchHandler, 1000, configGetter.batchFetchTimeThreshold(), TimeUnit.MILLISECONDS);
    }


    @Override
    public void execute(SendTextMessageRequest sendTextMessageRequest) {
        insertHandler.addToQueue(sendTextMessageRequest);
    }

    @Override
    public void execute(GetMessagesRequest getMessagesRequest) {
        fetchHandler.addToQueue(getMessagesRequest);
    }

}
