package ir.jibit.data_access;

import ir.jibit.presentation.request.GetMessagesRequest;
import ir.jibit.presentation.request.NotFoundRequest;
import ir.jibit.presentation.request.SendTextMessageRequest;
import ir.jibit.presentation.response.InformativeResponse;
import ir.jibit.presentation.response.StatusCode;

/**
 * This class has overloaded execute methods that take actions on data resource such as database and return responnnse
 *
 * @author mrahimian
 */
public interface Executable {
    /**
     * This method executes query to any resource such as database
     * It adds a message from an id to another
     *
     * @param sendTextMessageRequest this is data access input
     * @return result
     */
    void execute(SendTextMessageRequest sendTextMessageRequest);

    /**
     * This method executes query to any resource such as database
     * It fetches messages
     *
     * @param getMessagesRequest this is data access input
     * @return result
     */
    void execute(GetMessagesRequest getMessagesRequest);

    /**
     * This method relates to a request that doesn't matched to any route.
     * No resource connection needed.
     *
     * @param notFoundRequest this is data access input
     * @return result
     */
    default void execute(NotFoundRequest notFoundRequest) {
        ResponseQueue.getInstance().addElement(new InformativeResponse(notFoundRequest, StatusCode.NOT_FOUND, "Path not found."));
    }
}
