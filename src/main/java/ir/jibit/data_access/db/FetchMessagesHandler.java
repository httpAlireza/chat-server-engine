package ir.jibit.data_access.db;

import ir.jibit.Chat;
import ir.jibit.data_access.ResponseQueue;
import ir.jibit.presentation.request.GetMessagesRequest;
import ir.jibit.presentation.request.Request;
import ir.jibit.presentation.response.StatusCode;
import ir.jibit.presentation.response.TextMessageResponse;
import ir.jibit.presentation.response.message.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ir.jibit.Main.configGetter;

/**
 * This class is used to fetch messages from database
 */
public class FetchMessagesHandler extends DBInteract {

    private final static Logger logger = LoggerFactory.getLogger(FetchMessagesHandler.class);

    private final String GetMessagesQuery = "SELECT t.* FROM (SELECT * FROM chat WHERE unique_chat IN (%s)) AS t INNER JOIN " +
            "(SELECT unique_chat,MAX(message_number) AS maximum FROM chat WHERE unique_chat IN (%s) GROUP BY unique_chat) AS s " +
            "ON t.unique_chat = s.unique_chat AND t.message_number+15>s.maximum ";

    private final ExecutorService es;

    public FetchMessagesHandler(Queue requestsQueue) {
        super(requestsQueue);
        es = Executors.newFixedThreadPool(configGetter.fetchMessagesHandlerThreadNumber());
    }

    @Override
    public void addToQueue(Request request) {
        super.addToQueue(request);
        var size = configGetter.batchFetchSize();
        if (queueSize() == size) {
            executeQuery(getNItems(size), "size limit");
        }
    }


    /**
     * This method is used to create final query of database
     */
    private void addStatement(Request request, StringBuilder queryBuilder, Map<String, MapValue> requestMap) {
        GetMessagesRequest getMessagesRequest = (GetMessagesRequest) request;
        var unique_chat = getMessagesRequest.getUniqueChat();
        if (!requestMap.containsKey(unique_chat)) {
            queryBuilder.append(String.format("'%s',", getMessagesRequest.getUniqueChat()));
            var mapValue = new MapValue();
            mapValue.addRequest(getMessagesRequest);
            requestMap.put(unique_chat, mapValue);
            return;
        }
        requestMap.get(unique_chat).addRequest(getMessagesRequest);
    }


    @Override
    public void executeQuery(List<Request> input, String executeType) {
        es.submit(() -> {
            var startTime = System.currentTimeMillis();
            Map<String, MapValue> requestMap = new HashMap<>();
            final StringBuilder queryBuilder = new StringBuilder();
            input.forEach(item -> addStatement(item, queryBuilder, requestMap));
            var lastCharIndex = queryBuilder.length();
            var query = queryBuilder.replace(lastCharIndex - 1, lastCharIndex, "");
            try (Connection con = DataSource.getConnection();
                 PreparedStatement preparedStatement = con.prepareStatement(
                         String.format(GetMessagesQuery, query, query)
                 )) {
                var executeStartTime = System.currentTimeMillis();
                var result = preparedStatement.executeQuery();
                var executeFinishTime = System.currentTimeMillis();
                String unique_chat;
                while (result.next()) {
                    var chat = new Chat<>(result.getLong("from_user"),
                            result.getLong("to_user"),
                            result.getString("message"));
                    var timeStamp = result.getTimestamp("date");
                    unique_chat = result.getString("unique_chat");
                    var textMessage = new TextMessage(chat, timeStamp);
                    requestMap.get(unique_chat).addTextMessage(textMessage);
                }
                requestMap.forEach((key, value) -> {
                    value.sendResponseToResponseQueue(StatusCode.OK);
                });
                var finishTime = System.currentTimeMillis();
                logger.info(input.size() + " requests executed in " + (finishTime - startTime) + " ms through " + executeType
                + " | Database execute time: " + (executeFinishTime - executeStartTime) + "ms");
            } catch (SQLException e) {
                requestMap.forEach((key, value) -> {
                    value.sendResponseToResponseQueue(StatusCode.SERVER_ERROR);
                });
                logger.error(input.size() + " requests failed to execute.");
            }
        });
    }

    private class MapValue {
        private List<Request> requests;
        private List<TextMessage> textMessages;

        MapValue() {
            requests = new ArrayList<>();
            textMessages = new ArrayList<>();
        }


        void addRequest(Request request) {
            requests.add(request);
        }


        void addTextMessage(TextMessage textMessage) {
            this.textMessages.add(textMessage);
        }

        void sendResponseToResponseQueue(StatusCode statusCode) {

            requests.forEach(req -> ResponseQueue.getInstance().addElement(new TextMessageResponse(req, statusCode, textMessages)));
        }

    }
}
