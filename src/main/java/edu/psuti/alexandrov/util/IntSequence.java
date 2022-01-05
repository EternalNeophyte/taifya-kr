package edu.psuti.alexandrov.util;

public class IntSequence {

    private static int number = 0;

    public static int next() {
        return ++number;
    }

    public static int number() {
        return number;
    }

    public static void reset() {
        number = 0;
    }
}
