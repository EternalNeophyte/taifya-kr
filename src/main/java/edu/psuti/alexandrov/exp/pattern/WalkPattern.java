package edu.psuti.alexandrov.exp.pattern;

import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public abstract class WalkPattern<T> {

    Predicate<T> predicate;

    WalkPattern(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public abstract Prediction walk(int index, List<T> values);

    boolean canStep(int index, List<T> values) {
        return index < values.size() && predicate.test(values.get(index));
    }

    public static <T> UniquePattern<T> unique(Predicate<T> predicate) {
        return new UniquePattern<>(predicate);
    }

    public static <T> MaybeUniquePattern<T> maybeUnique(Predicate<T> predicate) {
        return new MaybeUniquePattern<>(predicate);
    }

    public static <T> MultiplePattern<T> multiple(Predicate<T> predicate) {
        return new MultiplePattern<>(predicate);
    }

    public static <T> MaybeMultiplePattern<T> maybeMultiple(Predicate<T> predicate) {
        return new MaybeMultiplePattern<>(predicate);
    }

    public static <T> RepeatablePattern<T> repeatable(Predicate<T> predicate, T[] row) {
        return new RepeatablePattern<>(predicate, row);
    }

    public static <T> MaybeRepeatablePattern<T> maybeRepeatable(Predicate<T> predicate, T[] row) {
        return new MaybeRepeatablePattern<>(predicate, row);
    }

}
