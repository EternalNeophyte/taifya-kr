package edu.psuti.alexandrov.exp;

public enum MatchingType {

    COMPLETE(0),
    PARTIAL(1),
    NO(2);

    private final int priority;

    MatchingType(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }
}
