package edu.psuti.alexandrov.interpret;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public interface StreamingEnum<V, S, T> {

    V[] valuesFactory();

    Stream<T> targetStream(Stream<V> values, List<S> source);

    default Stream<V> valuesAsStream() {
        return Arrays.stream(valuesFactory());
    }

    default List<T> find(List<S> source) {
        try(Stream<V> values = valuesAsStream()) {
            return targetStream(values, source).toList();
        }
    }
}
