package edu.psuti.alexandrov.exp;

public record Prediction(int index, boolean condition) {

    public static final Prediction DUMMY = new Prediction(0, true);

    public Prediction merge(Prediction other) {
        return new Prediction(other.index, this.condition && other.condition);
    }
}
