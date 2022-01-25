package edu.psuti.alexandrov.util;

import java.util.Arrays;
import java.util.function.IntFunction;

public class ArraySamples {

    @SafeVarargs
    public static <T> T[] merge(IntFunction<T[]> generator, T[]... samples) {
        return Arrays.stream(samples)
                .flatMap(Arrays::stream)
                .toArray(generator);
    }
}
