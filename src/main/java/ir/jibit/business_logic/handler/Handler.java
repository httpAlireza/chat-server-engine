package ir.jibit.business_logic.handler;

import java.util.function.Function;

public abstract class Handler<I, O> implements Function<I, O> {
    private String name;

    public Handler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
