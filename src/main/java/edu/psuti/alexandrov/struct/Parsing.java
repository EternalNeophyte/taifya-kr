package edu.psuti.alexandrov.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface Parsing<T> {

    String OR = "|";
    String SPLIT_SIGN = "s:";
    String SPACES = "\\s+";

    String EMPTY = "";
    String LEX_DELIMITER = "%";
    String DIRTY_LEX_SPLIT = "\\w+|[\\W&&[^%]]{0,2}|" + LEX_DELIMITER;
    String LINE_WRAP = "\n|\r\n";

    String IDENTIFIER = "[a-zA-Z][\\w]*";
    String FLOAT = "[\\d]*[.][\\d]+";
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
