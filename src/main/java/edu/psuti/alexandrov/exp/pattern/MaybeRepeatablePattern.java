package edu.psuti.alexandrov.exp.pattern;

import java.util.function.Predicate;

public class MaybeRepeatablePattern<T> extends AbstractRepeatablePattern<T> {

    MaybeRepeatablePattern(Predicate<T> predicate, T[] row) {
        super(predicate, row);
    }

    @Override
    boolean mergeConditions(boolean main, boolean additional) {
        return main || additional;
    }
}
