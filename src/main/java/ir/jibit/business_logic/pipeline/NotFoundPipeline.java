package ir.jibit.business_logic.pipeline;

import ir.jibit.business_logic.handler.Handler;
import ir.jibit.business_logic.handler.LogHandler;
import ir.jibit.presentation.request.NotFoundRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for handling wrong requests.
 *
 * @author Alireza khodadoust
 */

public class NotFoundPipeline extends Pipeline<NotFoundRequest, NotFoundRequest> {

    private static List<Handler> createHandlersList() {
        List<Handler> output = new ArrayList<>();
        output.add(new LogHandler("Not_Found_Handler"));
        //Handlers which should be in the pipeline should be added here by order.
        //Handlers list can also be empty.
        return output;
    }

    public NotFoundPipeline() {
        super(createHandlersList());
    }


}
