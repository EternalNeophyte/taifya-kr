package edu.psuti.alexandrov.exp;

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

    @Override
    public int compareTo(Matching other) {
        return Integer.compare(this.type.priority(), other.type.priority());
    }
}
