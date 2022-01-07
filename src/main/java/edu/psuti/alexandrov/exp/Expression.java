package edu.psuti.alexandrov.exp;

import edu.psuti.alexandrov.exp.pattern.WalkPattern;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Expression<T> {

    private final List<WalkPattern<T>> patterns;

    private Expression() {
        patterns = new LinkedList<>();
    }

    public static <T> Predicate<T> in(T[] values) {
        return checked -> {
            for(T value : values) {
                if(checked.equals(value)) {
                    return true;
                }
            }
            return false;
        };
    }

    public static <T> Expression<T> start() {
        return new Expression<>();
    }

    @SafeVarargs
    public final Expression<T> one(T... values) {
        patterns.add(WalkPattern.unique(in(values)));
        return this;
    }

    @SafeVarargs
    public final Expression<T> maybeOne(T... values) {
        patterns.add(WalkPattern.maybeUnique(in(values)));
        return this;
    }

    @SafeVarargs
    public final Expression<T> many(T... values) {
        patterns.add(WalkPattern.repeatable(in(values)));
        return this;
    }

    @SafeVarargs
    public final Expression<T> maybeMany(T... values) {
        patterns.add(WalkPattern.maybeRepeatable(in(values)));
        return this;
    }

    public final Expression<T> one(Expression<T> other) {

        return this;
    }

    public final Expression<T> maybeOne(Expression<T> other) {

        return this;
    }

    public Matching compute(List<T> values) {
        try(Stream<WalkPattern<T>> stream = patterns.stream()) {
            Prediction result = stream.reduce(
                    Prediction.DUMMY,
                    (prediction, pattern) -> pattern.walk(prediction.index(), values),
                    Prediction::merge
            );
            boolean condition = result.condition();
            return condition && result.index() == values.size()
                    ? Matching.COMPLETE
                    : condition
                        ? Matching.PARTIAL
                        : Matching.NO;
        }
    }
}
