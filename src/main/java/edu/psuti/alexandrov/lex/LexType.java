package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.struct.Parsing;

public enum LexType {

    UNKNOWN(0, Parsing.EMPTY),
    KEYWORD(1, "\\w+"),
    TYPE_DEF(1, "integer|real|boolean"),
    DELIMITER(2, "[\\W&&[^" + Parsing.LEX_DELIMITER + "]]{0,2}"),
    COMPARE_OP(2, "==|!=|>=|<=|>|<"),
    ADD_OP(2, "or|plus|minus|"),
    MULTIPLY_OP(2, "and|\\*|\\\\|"),
    IDENTIFIER(3, "[a-zA-Z][\\w]*"),
    BINARY_NUM(4, "[01]+[Bb]"),
    OCTET_NUM(4, "[0-7]+[Oo]"),
    HEX_NUM(4,"[\\da-fA-F]+[Hh]"),
    DECIMAL_NUM(4, "[\\d]+[Dd]?"),
    FLOAT_NUM(4, "[\\d]*[.][\\d]+([eE][+-]?[\\d])?|[\\d]+[eE][+-]?[\\d]");

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

    public static Expression<LexType> expression() {
        return Expression.start();
    }
}
