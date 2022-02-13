package edu.psuti.alexandrov.interpret;

import java.util.function.BiConsumer;

public interface BiAction<T, U> extends BiConsumer<T, U> {

    static <T, U> BiAction<T, U> emptyAction() {
        return (a, b) -> { };
    }
}
