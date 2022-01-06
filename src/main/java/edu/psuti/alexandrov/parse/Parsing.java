package edu.psuti.alexandrov.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface Parsing<T> {

    String EMPTY = "";
    String OR = "|";
    String SPACES = "\\s+";
    String LINE_WRAP = "\n|\r\n";
    String OPEN_COMMENT = "{";
    String CLOSE_COMMENT = "}";
    String SPLIT_SIGN = "s:";
    String LEX_DELIMITER = "%";
    String DIRTY_LEX_SPLIT = "\\w+|[\\W&&[^" + LEX_DELIMITER + "]]{0,2}";

    String mask();
    String input();
    T map(String sample);

    default Supplier<? extends List<T>> listFactory() {
        return ArrayList::new;
    }
}
