package ir.jibit.presentation.request;

import ir.jibit.Chat;

/**
 * After decoding requests in the decoder if it is a request to send a text message decoder will create
 * an object from this class and pass it to the dispatcher.
 *
 * @author Alireza Khodadoost
 */
public class SendTextMessageRequest extends Request {

    private Chat<String> chat;

    public SendTextMessageRequest(String method, Chat chat) {
        super(method);

        this.chat = chat;
    }

    public Long getFrom() {
        return chat.getFrom();
    }

    public Long getTo() {
        return chat.getTo();
    }

    public String getUniqueChat() {
        var minId = Math.min(chat.getFrom(), chat.getTo());
        var maxId = Math.max(chat.getFrom(), chat.getTo());
        return minId + "_" + maxId;
    }

    public String getTextMessage() {
        return chat.getMessage();
    }

    @Override
    public String logMessage() {
        return getMethod() + ": sending message from user " + getFrom() + " to user " + getTo();
    }
}
