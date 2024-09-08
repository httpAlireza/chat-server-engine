package ir.jibit.business_logic.pipeline;

import ir.jibit.business_logic.handler.Handler;
import ir.jibit.business_logic.handler.LogHandler;
import ir.jibit.presentation.request.GetMessagesRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for handling requests which have been sent for getting messages between two ids.
 *
 * @author mrahimian
 */

public class GetMessagesPipeline extends Pipeline<GetMessagesRequest, GetMessagesRequest> {

    private static List<Handler> createHandlersList() {
        List<Handler> output = new ArrayList<>();
        output.add(new LogHandler("Get_Message_Handler"));
        //Handlers which should be in the pipeline should be added here by order.
        //Handlers list can also be empty.
        return output;
    }

    public GetMessagesPipeline() {
        super(createHandlersList());
    }


}
