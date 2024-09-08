package ir.jibit.presentation.response;

import ir.jibit.presentation.request.Request;
import ir.jibit.presentation.response.message.TextMessage;

import java.util.List;

/**
 * This class is a type of response messages which map to <i>SendTextMessageRequest</i>
 *
 * @author mrahimian
 */
public class TextMessageResponse extends Response<List<TextMessage>> {

    public TextMessageResponse(Request request, StatusCode statusCode, List<TextMessage> textMessageList) {
        super(request, statusCode, textMessageList);
    }

}
