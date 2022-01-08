package edu.psuti.alexandrov.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface Parsing<T> {

    String EMPTY = "";
    String OR = "|";
    String SCREEN = "\\";
    String SPACES = "\\s+";
    String LINE_WRAP = "\n|\r\n";
    String OPEN_COMMENT = "{";
    String CLOSE_COMMENT = "}";
    String SPLIT_SIGN = "s:";
    String LEX_DELIMITER = "%";

    String mask();
    String input();
    T map(String sample);

    default Supplier<? extends List<T>> listFactory() {
        return ArrayList::new;
    }
}
