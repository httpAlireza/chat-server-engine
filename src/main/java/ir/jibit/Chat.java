package ir.jibit;

/**
 * This class contain parameters of a single message
 *
 * @param <T> type of message(String if text message)
 * @author mrahimian
 */
public class Chat<T> {
    private Long from;
    private Long to;
    private T message;

    public Chat(Long from, Long to, T message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public T getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MESSAGE : " + getMessage() + " FROM : " + getFrom() + " TO : " + getTo();
    }
}
