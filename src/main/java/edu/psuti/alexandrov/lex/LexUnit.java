package edu.psuti.alexandrov.lex;

import java.util.regex.MatchResult;

public record LexUnit(LexType type, MatchResult result) implements Comparable<LexUnit> {

    @Override
    public int compareTo(LexUnit other) {
        return Integer.compare(this.result.start(), other.result.start());
    }
}
