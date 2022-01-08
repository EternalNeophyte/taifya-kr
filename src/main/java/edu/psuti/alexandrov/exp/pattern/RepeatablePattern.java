package edu.psuti.alexandrov.exp.pattern;

import java.util.function.Predicate;

public class RepeatablePattern<T> extends AbstractRepeatablePattern<T> {

    RepeatablePattern(Predicate<T> predicate, T[] row) {
        super(predicate, row);
    }

    @Override
    boolean mergeConditions(boolean main, boolean additional) {
        return main && additional;
    }
}
