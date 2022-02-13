package edu.psuti.alexandrov.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created on 19.01.2022 by
 *
 * @author alexandrov
 */
public record BiBuffer<T1, T2>(List<T1> firstHalf, List<T2> secondHalf) {

    public static <T1, T2> BiBuffer<T1, T2> basedOnLinkedList() {
        return new BiBuffer<>(new LinkedList<>(), new LinkedList<>());
    }

    public static <T1, T2> BiBuffer<T1, T2> basedOnArrayList() {
        return new BiBuffer<>(new ArrayList<>(), new ArrayList<>());
    }

    public record Pair<T1, T2>(T1 firstItem, T2 secondItem) {

    }

    public void put(T1 firstItem, T2 secondItem) {
        firstHalf.add(firstItem);
        secondHalf.add(secondItem);
    }

    public Pair<T1, T2> get(int index) {
        return new Pair<>(firstHalf.get(index), secondHalf.get(index));
    }

    public void clear() {
        firstHalf.clear();
        secondHalf.clear();
    }

    public boolean isEmpty() {
        return firstHalf.isEmpty() && secondHalf.isEmpty();
    }

    public void forEach(BiConsumer<T1, T2> pairConsumer) {
        for(int i = 0; i < firstHalf.size(); i++) {
            pairConsumer.accept(firstHalf.get(i), secondHalf.get(i));
        }
    }

    public List<T1> copyFirstHalf() {
        return List.copyOf(firstHalf);
    }

    public List<T2> copySecondHalf() {
        return List.copyOf(secondHalf);
    }
}
