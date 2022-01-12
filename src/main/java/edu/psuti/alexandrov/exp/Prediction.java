package edu.psuti.alexandrov.exp;

public record Prediction(int index, boolean condition) {

    public static final Prediction DUMMY = new Prediction(0, true);

    public Prediction merge(Prediction other) {
        return new Prediction(other.index, this.condition && other.condition);
    }

    public Prediction increment() {
        return new Prediction(this.index + 1, this.condition);
    }

    public Prediction decrement() {
        return new Prediction(this.index - 1, this.condition);
    }

    public Prediction negate() {
        return new Prediction(this.index, !this.condition);
    }
}
