package ir.jibit.business_logic;

import ir.jibit.business_logic.pipeline.GetMessagesPipeline;
import ir.jibit.business_logic.pipeline.NotFoundPipeline;
import ir.jibit.business_logic.pipeline.SendTextMessagePipeline;
import ir.jibit.data_access.DataAccess;
import ir.jibit.presentation.request.GetMessagesRequest;
import ir.jibit.presentation.request.NotFoundRequest;
import ir.jibit.presentation.request.SendTextMessageRequest;

public class Dispatcher {

    private final DataAccess dataAccess;
    private final GetMessagesPipeline getMessagesPipeline;
    private final SendTextMessagePipeline sendTextMessagePipeline;
    private final NotFoundPipeline notFoundPipeline;

    public Dispatcher() {
        dataAccess = new DataAccess();
        getMessagesPipeline = new GetMessagesPipeline();
        sendTextMessagePipeline = new SendTextMessagePipeline();
        notFoundPipeline = new NotFoundPipeline();
    }

    public void handle(SendTextMessageRequest sendTextMessageRequest) {
        var pipelineOutput = sendTextMessagePipeline.apply(sendTextMessageRequest);
        dataAccess.run(pipelineOutput);
    }

    public void handle(GetMessagesRequest getMessagesRequest) {
        var pipelineOutput = getMessagesPipeline.apply(getMessagesRequest);
        dataAccess.run(pipelineOutput);
    }

    public void handle(NotFoundRequest notFoundRequest) {
        var pipelineOutput = notFoundPipeline.apply(notFoundRequest);
        dataAccess.run(pipelineOutput);
    }
}
