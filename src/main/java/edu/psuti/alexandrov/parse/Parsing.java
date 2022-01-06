package edu.psuti.alexandrov.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface Parsing<T> {

    String EMPTY = "";
    String OR = "|";
    String SPACES = "\\s+";
    String LINE_WRAP = "\n|\r\n";
    String SPLIT_SIGN = "s:";
    String LEX_DELIMITER = "%";
    String DIRTY_LEX_SPLIT = "\\w+|[\\W&&[^" + LEX_DELIMITER + "]]{0,2}";

    String FLOAT = "[\\d]*[.][\\d]+([eE]?|[eE][+-]?)";
    String BINARY = "[01]+[Bb]";
    String OCTET = "[0-7]+[Oo]";
    String DECIMAL = "[\\d]+[Dd]?";
    String HEX = "[\\da-fA-F]+[Hh]";

    String mask();
    String input();
    T map(String sample);

    default Supplier<? extends List<T>> listFactory() {
        return ArrayList::new;
    }
}
