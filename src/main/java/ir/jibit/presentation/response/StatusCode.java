package ir.jibit.presentation.response;

public enum StatusCode {
    OK(200, "OK"), BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"), SERVER_ERROR(500, "Server Error");

    private int code;

    private String statusMessage;

    StatusCode(int code, String statusMessage) {
        this.code = code;
        this.statusMessage = statusMessage;
    }

    public int getCode() {
        return code;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
