package edu.psuti.alexandrov.exp;

import java.util.function.Consumer;

public class Matching implements Comparable<Matching> {

    private final int index;
    private final MatchingType type;

    public Matching(Prediction prediction, int bound) {
        this.index = prediction.index();
        this.type = prediction.condition()
                ? index == bound
                        ? MatchingType.COMPLETE
                        : MatchingType.PARTIAL
                : MatchingType.NO;
    }

    public int index() {
        return index;
    }

    public MatchingType type() {
        return type;
    }

    public boolean isComplete() {
        return type.equals(MatchingType.COMPLETE);
    }

    public boolean isPartial() {
        return type.equals(MatchingType.PARTIAL);
    }

    public boolean isNone() {
        return type.equals(MatchingType.NO);
    }

    private Matching ifTypeEquals(MatchingType other, Consumer<Matching> consumer) {
        if(type.equals(other)) {
            consumer.accept(this);
        }
        return this;
    }

    public Matching ifComplete(Consumer<Matching> consumer) {
        return ifTypeEquals(MatchingType.COMPLETE, consumer);
    }

    public Matching ifPartial(Consumer<Matching> consumer) {
        return ifTypeEquals(MatchingType.PARTIAL, consumer);
    }

    public Matching ifNo(Consumer<Matching> consumer) {
        return ifTypeEquals(MatchingType.NO, consumer);
    }

    @Override
    public int compareTo(Matching other) {
        return Integer.compare(this.type.priority(), other.type.priority());
    }
}
