package edu.psuti.alexandrov.exp;

public class SkipMergingException extends RuntimeException {

    private final Prediction transported;

    public SkipMergingException(Prediction transported) {
        this.transported = transported;
    }

    public Prediction transported() {
        return transported;
    }
}
