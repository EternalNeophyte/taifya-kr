package edu.psuti.alexandrov.exp;

public class Matching {

    private final int index;
    private final MatchingType type;

    public Matching(Prediction prediction, int bound) {
        this.index = prediction.index() - 1;
        this.type = prediction.condition()
                ? index == bound - 1
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
}
