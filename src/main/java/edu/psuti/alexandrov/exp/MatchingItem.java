package edu.psuti.alexandrov.exp;

public record MatchingItem<T>(Matching matching, T item) implements Comparable<MatchingItem<T>> {


    @Override
    public int compareTo(MatchingItem<T> other) {
        return this.matching.compareTo(other.matching);
    }
}
