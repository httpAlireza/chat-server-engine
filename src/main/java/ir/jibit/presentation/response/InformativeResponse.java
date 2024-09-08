package ir.jibit.presentation.response;

import ir.jibit.presentation.request.Request;

public class InformativeResponse extends Response<String> {
    public InformativeResponse(Request request, StatusCode statusCode, String message) {
        super(request, statusCode, message);
    }
}
