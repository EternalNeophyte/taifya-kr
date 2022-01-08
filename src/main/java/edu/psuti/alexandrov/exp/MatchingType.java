package edu.psuti.alexandrov.exp;

public enum MatchingType {

    COMPLETE(2),
    PARTIAL(1),
    NO(0);

    private final int priority;

    MatchingType(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }
}
