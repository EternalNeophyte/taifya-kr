package edu.psuti.alexandrov.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface Parsing<T> {

    String SPLIT_SIGN = "s:";
    String SPACES = "\\s+";
    String SPLIT_SPACES = "s:\\s+";

    String mask();
    String input();
    T map(String sample);

    default Supplier<? extends List<T>> listFactory() {
        return ArrayList::new;
    }
}
