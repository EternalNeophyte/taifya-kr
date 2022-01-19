package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.exp.Expression;

import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;

public enum LexType {

    COMMENT_BODY(0, compile("[{][\\s\\S]*[}]")),
    LOGIC_CONST(1, compile("true|false")),
    TYPE_DEF(1, compile("integer|real|boolean")),
    ASSIGN_DEF(1, compile("let")),
    IF_DEF(1, compile("if")),
    THEN_SECTION(1, compile("then")),
    ELSE_SECTION(1, compile("else")),
    END_IF(1, compile("end_else")),
    FOR_LOOP_DEF(1, compile("for")),
    WHILE_LOOP_DEF(1, compile("do while")),
    END_WHILE_LOOP(1, compile("loop")),
    INPUT_DEF(1, compile("input")),
    OUTPUT_DEF(1, compile("output")),
    DELIMITER(2, compile("[:.]")),
    COMPARE_OP(2, compile("==|!=|>=|<=|>|<")),
    ADD_OP(2, compile("or|plus|minus")),
    MULTIPLY_OP(2, compile("and|\\*|\\\\")),
    ASSIGN_OP(2, compile("[=]")),
    UNARY_OP(2, compile("[!]")),
    LISTING(2, compile("[,]")),
    START_ARGS(2, compile("[(]")),
    END_ARGS(2, compile("[)]")),
    END_STATEMENT(2, compile("[;]")),
    BINARY_NUM(4, compile("[01]+[Bb]")),
    OCTET_NUM(4, compile("[0-7]+[Oo]")),
    HEX_NUM(4,compile("[\\da-fA-F]+[Hh]")),
    DECIMAL_NUM(4, compile("[\\d]+[Dd]?")),
    FLOAT_NUM(4, compile("[\\d]*[.][\\d]+([eE][+-]?[\\d])?|[\\d]+[eE][+-]?[\\d]")),
    IDENTIFIER(3, compile("[a-zA-Z][\\w]*")),
    RAW_STATEMENT(0, compile("[\\s\\S&&[^%]]+"));


    public static final LexType[] OPERAND = new LexType[] {
            IDENTIFIER, FLOAT_NUM, BINARY_NUM, OCTET_NUM, DECIMAL_NUM, HEX_NUM
    };

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
        return Arrays.stream(values());
    }

    public static Expression<LexType> expression() {
        return Expression.start();
    }
}
