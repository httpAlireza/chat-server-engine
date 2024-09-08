package ir.jibit.business_logic.pipeline;

import ir.jibit.business_logic.handler.Handler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * It is used for handling business logic of any type of request.
 * for adding default handlers to all the pipelines they should be instantiated by their order in <i>DEFAULT_HANDLERS</i>
 * array on the top of this class.
 *
 * @author mrahimian
 */
public class Pipeline<I, O> {

    private final static Handler[] DEFAULT_HANDLERS = {};

    private final LinkedList<Handler> handlers;

    public Pipeline(List<Handler> handlers) {
        this.handlers = new LinkedList<>();
        Arrays.stream(DEFAULT_HANDLERS).forEach(handler -> this.handlers.add(handler));
        handlers.stream().forEach(handler -> this.handlers.add(handler));
    }

    /**
     * Run the pipeline.
     *
     * @param input input
     * @return output
     */
    public O apply(I input) {
        return handlers.isEmpty() ? (O) input : (O) handlers.getFirst().apply(input);
    }


    public void addHandler(Handler handler) {
        if (!handlers.isEmpty()) {
            handlers.getLast().andThen(handler);
        }
        handlers.add(handler);
    }

    public void addAfter(String handlerName, Handler handler) {
        for (int i = 0; i < handlers.size(); i++) {
            if (handlers.get(i).getName().equals(handlerName)) {
                var before = handlers.get(i);
                before.andThen(handler);
                if (i + 1 != handlers.size()) {
                    var after = handlers.get(i + 1);
                    handler.andThen(after);
                }
                handlers.add(i + 1, handler);
            }
        }
    }
}
