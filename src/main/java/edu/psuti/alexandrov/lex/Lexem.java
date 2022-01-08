package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.util.IntSequence;

public record Lexem(int id, String value) {

    public Lexem(String value) {
        this(IntSequence.next(), value);
    }
}
