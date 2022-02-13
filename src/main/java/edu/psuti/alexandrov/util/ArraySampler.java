package edu.psuti.alexandrov.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

public class ArraySampler<T> {

    private IntFunction<T[]> generator;

    private ArraySampler(IntFunction<T[]> generator) {
        this.generator = generator;
    }

    public static <T> ArraySampler<T> setup(IntFunction<T[]> generator) {
        return new ArraySampler<>(generator);
    }

    @SafeVarargs
    public final T[] merge(T[]... samples) {
        return Arrays.stream(samples)
                .flatMap(Arrays::stream)
                .toArray(generator);
    }

    @SafeVarargs
    public final T[] exclude(T[] source, T... values) {
        List<T> stopList = Arrays.asList(values);
        return Arrays.stream(source)
                .filter(v -> !stopList.contains(v))
                .toArray(generator);
    }
}
