package ir.jibit.business_logic.handler;

import ir.jibit.presentation.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHandler extends Handler<Request, Request> {

    private final static Logger logger = LoggerFactory.getLogger(LogHandler.class);

    public LogHandler(String name) {
        super(name);
    }

    @Override
    public Request apply(Request request) {
        logger.debug(request.logMessage());
        return request;
    }
}
