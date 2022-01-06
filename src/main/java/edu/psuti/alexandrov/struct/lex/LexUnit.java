package edu.psuti.alexandrov.struct.lex;

public record LexUnit(LexType type, Lexem lexem) {

    public static LexUnit unknown(String value) {
        return new LexUnit(LexType.UNKNOWN, new Lexem(value));
    }
}
