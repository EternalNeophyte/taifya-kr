package edu.psuti.alexandrov.struct.lex;

public record LexUnit(LexType type, Lexem lexem) {

    public static final LexUnit UNKNOWN = new LexUnit(null, null);
}
