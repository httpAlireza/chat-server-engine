package ir.jibit.presentation.request;

public class NotFoundRequest extends Request {
    public NotFoundRequest(String method) {
        super(method);
    }


    @Override
    public String logMessage() {
        return "Incoming request doesn't match any route.";
    }
}
