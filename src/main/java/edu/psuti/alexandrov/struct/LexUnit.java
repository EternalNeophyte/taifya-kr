package edu.psuti.alexandrov.struct;

public record LexUnit(int tableNum, Lexem lexem) {

    public static final LexUnit UNKNOWN = new LexUnit(0, null);
}
