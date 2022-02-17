package edu.psuti.alexandrov.lex;

import java.util.regex.MatchResult;

import static java.util.Objects.nonNull;

public record LexUnit(LexType type, MatchResult result) implements Comparable<LexUnit> {

    @Override
    public int compareTo(LexUnit other) {
        return Integer.compare(this.result.start(), other.result.start());
    }

    @Override
    public String toString() {
        return nonNull(result) ? result.group() : "";
    }
}
