package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.exp.Expression;

import java.awt.*;
import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;

public enum LexType implements LexHighlighting {

    COMMENT_BODY(0, "Блок комментариев", compile("[{][\\s\\S]*[}]"), GRAY_DUST),

    LOGIC_CONST(1, "Логическая константа", compile("true|false"), ARCTIC_GRASS),
    TYPE_DEF(1, "Объявление типа", compile("integer|real|boolean"), LILY),
    ASSIGN_DEF(1, "Объявление присваивания", compile("let"), LILY),
    IF_DEF(1, "Объявление условного оператора", compile("if"), LILY),
    END_IF(1, "Конец условного оператора", compile("end_else"), LILY),
    THEN_SECTION(1, "Секция 'then' условного оператора", compile("then"), LILY),
    ELSE_SECTION(1, "Секция 'else' условного оператора", compile("else"), LILY),
    FOR_LOOP_DEF(1, "Объявление цикла 'for'", compile("for"), LILY),
    WHILE_LOOP_DEF(1, "Объявление цикла 'while'", compile("do while"), LILY),
    END_WHILE_LOOP(1, "Конец цикла 'while'", compile("loop"), LILY),
    INPUT_DEF(1, "Объявление оператора ввода", compile("input"), SAND),
    OUTPUT_DEF(1, "Объявление оператора вывода", compile("output"), SAND),

    FLOAT_NUM(4, "Вещественное число",
            compile("[\\d]*[.][\\d]+([eE][+-]?[\\d])?|[\\d]+[eE][+-]?[\\d]"), ARCTIC_GRASS),
    BINARY_NUM(4, "Двоичное целое число", compile("[01]+[Bb]"), GRASS),
    OCTET_NUM(4, "Восьмеричное целое число", compile("[0-7]+[Oo]"), GRASS),
    HEX_NUM(4, "Шестнадцатиричное целое число", compile("[\\da-fA-F]+[Hh]"), GRASS),
    DECIMAL_NUM(4, "Десятичное целое число", compile("[\\d]+[Dd]?"), GRASS),

    DELIMITER(2, "Разделитель", compile("[:.]"), SAKURA_SNOW),
    COMPARE_OP(2, "Операция сравнения", compile("==|!=|>=|<=|>|<"), SAKURA_SNOW),
    ADD_OP(2, "Операция сложения", compile("or|plus|minus"), SAKURA_SNOW),
    MULTIPLY_OP(2, "Операция умножения", compile("and|\\*|/"), SAKURA_SNOW),
    ASSIGN_OP(2, "Операция присваивания", compile("[=]"), SAKURA_SNOW),
    UNARY_OP(2, "Унарная операция", compile("[!]"), SAKURA_SNOW),
    LISTING(2, "Перечисление", compile("[,]"), SAKURA_SNOW),
    START_ARGS(2, "Начало группы аргументов", compile("[(]"), SAND),
    END_ARGS(2, "Конец группы аргументов", compile("[)]"), SAND),
    END_STATEMENT(2, "Конец утверждения", compile("[;]"), LILY),
    END_PROGRAM(0, "Конец программы", compile("end"), LILY),

    IDENTIFIER(3, "Идентификатор", compile("[a-zA-Z][\\w]*"), SKY_BLUE);

    private final int tableNum;
    private final String description;
    private final Pattern pattern;
    private final Color highlight;

    LexType(int tableNum, String description, Pattern pattern, Color highlight) {
        this.tableNum = tableNum;
        this.description = description;
        this.pattern = pattern;
        this.highlight = highlight;
    }

    public int tableNum() {
        return tableNum;
    }

    public Pattern pattern() {
        return pattern;
    }

    public Color highlight() {
        return highlight;
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

    public static boolean equals(LexType first, LexType second) {
        return first.equals(second);
    }

    public static boolean notEquals(LexType first, LexType second) {
        return !first.equals(second);
    }

    @Override
    public String toString() {
        return description;
    }
}
