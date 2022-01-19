package edu.psuti.alexandrov.exp;

import edu.psuti.alexandrov.exp.pattern.WalkPattern;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Expression<T> {

    private final List<WalkPattern<T>> patterns;

    Expression() {
        patterns = new LinkedList<>();
    }

    public Stream<WalkPattern<T>> patterns() {
        return patterns.stream();
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
        patterns.add(WalkPattern.multiple(in(values)));
        return this;
    }

    @SafeVarargs
    public final Expression<T> maybeMany(T... values) {
        patterns.add(WalkPattern.maybeMultiple(in(values)));
        return this;
    }

    @SafeVarargs
    public final Expression<T> carousel(T... values) {
        patterns.add(WalkPattern.repeatable(in(values), values));
        return this;
    }

    @SafeVarargs
    public final Expression<T> maybeCarousel(T... values) {
        patterns.add(WalkPattern.maybeRepeatable(in(values), values));
        return this;
    }

    @SafeVarargs
    public final Matching compute(T... values) {
        return compute(Arrays.asList(values));
    }

    public Matching compute(List<T> values) {
        try(Stream<WalkPattern<T>> patterns = patterns()) {
            Prediction result = patterns.reduce(
                    Prediction.DUMMY,
                    (prediction, pattern) -> {
                        Prediction next = pattern.walk(prediction.index(), values);
                        if(!next.condition()) {
                            throw new SkipMergingException(next);
                        }
                        return next;
                    },
                    Prediction::merge
            );
            return new Matching(result, values.size());
        }
        catch (SkipMergingException e) {
            Prediction transported = e.transported();
            return new Matching(transported, values.size());
        }
    }
}
