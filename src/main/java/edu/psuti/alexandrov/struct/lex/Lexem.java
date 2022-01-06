package edu.psuti.alexandrov.struct.lex;

import edu.psuti.alexandrov.util.IntSequence;

public record Lexem(int id, String value) {

    public Lexem(String value) {
        this(IntSequence.next(), value);
    }
}
