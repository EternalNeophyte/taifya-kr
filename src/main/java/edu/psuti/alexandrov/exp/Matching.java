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

    @Override
    public int compareTo(Matching other) {
        return Integer.compare(this.type.priority(), other.type.priority());
    }
}
