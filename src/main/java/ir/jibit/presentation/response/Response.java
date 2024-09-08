package ir.jibit.presentation.response;

import ir.jibit.presentation.request.Request;

/**
 * This abstract class is meant to have response of our requests as java objects which later in encoder will be encoded
 * as a network response.
 *
 * @param <T> type of message of response which can be a String , a List or anything else.
 * @author Alireza khodadoust
 */
public abstract class Response<T> {

    private Request request;
    private StatusCode statusCode;

    private T message;

    public Response(Request request, StatusCode statusCode, T message) {
        this.request = request;
        this.statusCode = statusCode;
        this.message = message;
    }

    public Response() {
        statusCode = null;
        request = null;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public T getMessage() {
        return message;
    }
}
