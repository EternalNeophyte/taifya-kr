package edu.psuti.alexandrov.lex;

public class IllegalLexException extends IllegalArgumentException {

    private final LexUnit unit;

    public IllegalLexException(String s, LexUnit unit) {
        super(s);
        this.unit = unit;
    }

    public LexUnit unit() {
        return unit;
    }
}
