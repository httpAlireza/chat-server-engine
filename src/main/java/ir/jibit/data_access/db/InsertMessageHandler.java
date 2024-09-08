package ir.jibit.data_access.db;

import ir.jibit.data_access.ResponseQueue;
import ir.jibit.presentation.request.Request;
import ir.jibit.presentation.request.SendTextMessageRequest;
import ir.jibit.presentation.response.InformativeResponse;
import ir.jibit.presentation.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ir.jibit.Main.configGetter;

/**
 * This class is used to insert messages to database
 */
public class InsertMessageHandler extends DBInteract {

    private final static Logger logger = LoggerFactory.getLogger(InsertMessageHandler.class);
    private final ExecutorService es;
    private final String SendTextMessageQuery = "INSERT INTO chat(from_user,to_user,message,date,unique_chat) VALUES (?,?,?,?,?)";

    public InsertMessageHandler(Queue requestsQueue) {
        super(requestsQueue);
        es = Executors.newFixedThreadPool(configGetter.insertMessageHandlerThreadNumber());
    }

    @Override
    public void addToQueue(Request request) {
        super.addToQueue(request);
        var size = configGetter.batchInsertSize();
        if (queueSize() == size) {
            executeQuery(getNItems(size), "size limit");
        }
    }


    private void addStatement(Request request, PreparedStatement preparedStatement) throws SQLException {
        SendTextMessageRequest sendTextMessageRequest = (SendTextMessageRequest) request;
        preparedStatement.setLong(1, sendTextMessageRequest.getFrom());
        preparedStatement.setLong(2, sendTextMessageRequest.getTo());
        preparedStatement.setString(3, sendTextMessageRequest.getTextMessage());
        preparedStatement.setTimestamp(4, new Timestamp(sendTextMessageRequest.getExecution_start_time()));
        preparedStatement.setString(5, sendTextMessageRequest.getUniqueChat());

        preparedStatement.addBatch();
    }


    @Override
    public void executeQuery(List<Request> input, String executeType) {
        es.execute(() -> {
            var startTime = System.currentTimeMillis();
            var size = input.size();
            try (Connection con = DataSource.getConnection(); PreparedStatement preparedStatement = con.prepareStatement(SendTextMessageQuery)) {
                input.forEach(item -> {
                    try {
                        addStatement(item, preparedStatement);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                var response = preparedStatement.executeBatch();
                for (int i = 0; i < size; i++) {
                    ResponseQueue.getInstance().addElement(response[i] == 1 ?
                            new InformativeResponse(input.get(i), StatusCode.OK, "Successfully Inserted. ") :
                            new InformativeResponse(input.get(i), StatusCode.SERVER_ERROR, "Insertion Unsuccessful"));
                }
                var finishTime = System.currentTimeMillis();
                logger.info(input.size() + " requests executed in " + (finishTime - startTime) + " ms through " + executeType);
            } catch (Exception e) {
                for (int i = 0; i < size; i++) {
                    ResponseQueue.getInstance().addElement(new InformativeResponse(input.get(i),
                            StatusCode.SERVER_ERROR, "Insertion Unsuccessful"));
                }
                logger.error(input.size() + " requests failed to execute.");
            }

        });
    }

}
