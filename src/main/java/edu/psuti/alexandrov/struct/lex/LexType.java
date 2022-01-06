package edu.psuti.alexandrov.struct.lex;

import edu.psuti.alexandrov.parse.Parsing;

public enum LexType {

    UNKNOWN(0, Parsing.EMPTY),
    KEYWORD(1, "\\w+"),
    DELIMITER(2, Parsing.DIRTY_LEX_SPLIT),
    IDENTIFIER(3, "[a-zA-Z][\\w]*"),
    BINARY_NUM(4, "[01]+[Bb]"),
    OCTET_NUM(4, "[0-7]+[Oo]"),
    HEX_NUM(4,"[\\da-fA-F]+[Hh]"),
    DECIMAL_NUM(4, "[\\d]+[Dd]?"),
    FLOAT_NUM(4, "[\\d]*[.][\\d]+([eE]?|[eE][+-]?)");

    LexType(int tableNum, String mask) {
        this.tableNum = tableNum;
        this.mask = mask;
    }

    private final int tableNum;
    private final String mask;

    public int tableNum() {
        return tableNum;
    }

    public String mask() {
        return mask;
    }
}
