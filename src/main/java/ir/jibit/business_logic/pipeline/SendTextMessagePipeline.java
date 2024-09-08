package ir.jibit.business_logic.pipeline;

import ir.jibit.business_logic.handler.Handler;
import ir.jibit.business_logic.handler.LogHandler;
import ir.jibit.presentation.request.SendTextMessageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for handling requests which have been sent for sending message from an id to another.
 * Singleton design pattern has been used in this class.
 *
 * @author mrahimian
 */

public class SendTextMessagePipeline extends Pipeline<SendTextMessageRequest, SendTextMessageRequest> {

    private static List<Handler> createHandlersList() {
        List<Handler> output = new ArrayList<>();
        output.add(new LogHandler("Send_Text_Message_Handler"));
        //Handlers which should be in the pipeline should be added here by order.
        //Handlers list can also be empty.
        return output;
    }


    public SendTextMessagePipeline() {
        super(createHandlersList());
    }


}
