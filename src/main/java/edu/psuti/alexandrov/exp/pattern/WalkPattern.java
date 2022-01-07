package edu.psuti.alexandrov.exp.pattern;

import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public abstract class WalkPattern<T> {

    Predicate<T> predicate;

    WalkPattern(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    abstract Prediction retrieve(int index, List<T> values);

    boolean test(int index, List<T> values) {
        return predicate.test(values.get(index));
    }

    public Prediction walk(int index, List<T> values) {
        return index >= values.size()
                ? new Prediction(index - 1, true)
                : retrieve(index, values);
    }

    public static <T> UniquePattern<T> unique(Predicate<T> predicate) {
        return new UniquePattern<>(predicate);
    }

    public static <T> MaybeUniquePattern<T> maybeUnique(Predicate<T> predicate) {
        return new MaybeUniquePattern<>(predicate);
    }

    public static <T> RepeatablePattern<T> repeatable(Predicate<T> predicate) {
        return new RepeatablePattern<>(predicate);
    }

    public static <T> MaybeRepeatablePattern<T> maybeRepeatable(Predicate<T> predicate) {
        return new MaybeRepeatablePattern<>(predicate);
    }

}
