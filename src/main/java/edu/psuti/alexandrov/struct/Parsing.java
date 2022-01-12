package edu.psuti.alexandrov.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface Parsing<T> {

    String EMPTY = "";
    String OR = "|";
    String SCREEN = "\\";
    String SPACES = "\\s+";
    String WORD_CHARS = "\\w+";
    String LINE_WRAP = "\r\n";
    String OPEN_COMMENT = "{";
    String CLOSE_COMMENT = "}";
    String LEX_DELIMITER = "%";

    String mask();
    String input();
    T map(String sample);

    default Supplier<? extends List<T>> listFactory() {
        return ArrayList::new;
    }
}
