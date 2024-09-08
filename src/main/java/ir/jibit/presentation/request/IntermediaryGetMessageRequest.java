package ir.jibit.presentation.request;

/**
 * This class is only an intermediary object that will be used in creating GetMessageRequest objects, because all the
 * attributes of GetMessageRequest won't be in the json that has been received from network at first Gson creates a
 * new object from this class and then this object will get passed to GetMessageRequest constructor.
 *
 * @author Alireza khodadoust
 */
public class IntermediaryGetMessageRequest {
    private Long id1;

    private Long id2;

    public IntermediaryGetMessageRequest(Long id1, Long id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public Long getId1() {
        return id1;
    }

    public void setId1(Long id1) {
        this.id1 = id1;
    }

    public Long getId2() {
        return id2;
    }

    public void setId2(Long id2) {
        this.id2 = id2;
    }
}
