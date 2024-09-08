package ir.jibit.presentation.request;

/**
 * After decoding requests in the decoder if it is a request to fetch messages between 2 ids decoder will create
 * an object from this class and pass it to the dispatcher.
 *
 * @author Alireza Khodadoost
 */
public class GetMessagesRequest extends Request {
    private Long id1;

    private Long id2;

    public GetMessagesRequest(String method, IntermediaryGetMessageRequest intermediaryGetMessageRequest) {
        super(method);
        this.id1 = intermediaryGetMessageRequest.getId1();
        this.id2 = intermediaryGetMessageRequest.getId2();
    }

    public Long getId1() {
        return id1;
    }

    public Long getId2() {
        return id2;
    }

    public String getUniqueChat() {
        var minId = Math.min(id1, id2);
        var maxId = Math.max(id1, id2);
        return minId + "_" + maxId;
    }


    @Override
    public String logMessage() {
        return getMethod() + ": Fetching messages between user " + getId1() + " and user " + getId2();
    }
}
