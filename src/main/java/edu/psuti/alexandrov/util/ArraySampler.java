package edu.psuti.alexandrov.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

public record ArraySampler<T>(IntFunction<T[]> generator) {

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
