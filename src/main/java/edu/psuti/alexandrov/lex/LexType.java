package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.exp.Expression;

import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;

public enum LexType {

    COMMENT_BLOCK(0, compile("[{][\\s\\S]*[}]")),
    KEYWORD(1, compile("\\w+|for")),
    TYPE_DEF(1, compile("integer|real|boolean")),
    DELIMITER(2, compile("[;:.,]")),
    COMPARE_OP(2, compile("==|!=|>=|<=|>|<")),
    ADD_OP(2, compile("or|plus|minus")),
    MULTIPLY_OP(2, compile("and|\\*|\\\\")),
    BINARY_NUM(4, compile("[01]+[Bb]")),
    OCTET_NUM(4, compile("[0-7]+[Oo]")),
    HEX_NUM(4,compile("[\\da-fA-F]+[Hh]")),
    DECIMAL_NUM(4, compile("[\\d]+[Dd]?")),
    FLOAT_NUM(4, compile("[\\d]*[.][\\d]+([eE][+-]?[\\d])?|[\\d]+[eE][+-]?[\\d]")),
    IDENTIFIER(3, compile("[a-zA-Z][\\w]*"));

    private static final Stream<LexType> ALL = Arrays.stream(values());
    private final int tableNum;
    private final Pattern pattern;

    LexType(int tableNum, Pattern pattern) {
        this.tableNum = tableNum;
        this.pattern = pattern;
    }

    public int tableNum() {
        return tableNum;
    }

    public Pattern pattern() {
        return pattern;
    }

    public Stream<MatchResult> match(String content) {
        return pattern.matcher(content)
                .results();
    }

    public static Stream<LexType> all() {
        return ALL;
    }

    public static Expression<LexType> expression() {
        return Expression.start();
    }
}
