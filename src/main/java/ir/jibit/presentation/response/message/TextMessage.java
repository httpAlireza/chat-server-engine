package ir.jibit.presentation.response.message;

import ir.jibit.Chat;

import java.sql.Timestamp;
import java.util.Date;


public class TextMessage {
    private Chat<String> chat;
    private Timestamp date;

    public TextMessage(Chat<String> chat, Timestamp date) {
        this.chat = chat;
        this.date = date;
    }

    public Chat<String> getChat() {
        return chat;
    }

    public Date getDate() {
        return date;
    }
}
