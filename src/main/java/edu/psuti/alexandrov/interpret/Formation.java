package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static edu.psuti.alexandrov.lex.LexType.*;

public enum Formation {

    VAR_DEF(expression()
            .one(IDENTIFIER)
            .maybeCarousel(LISTING, IDENTIFIER)
            .one(DELIMITER)
            .one(TYPE_DEF)
            .one(END_STATEMENT)),

    VAR_ASSIGN_VALUE(expression()
            .maybeOne(ASSIGN_DEF)
            .one(IDENTIFIER)
            .one(ASSIGN_OP)
            .one(OPERAND)),

    COMPARISION(expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(COMPARE_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS)),

    COMPARISION_EXTRA_OP(expression()
            .one(ADD_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS)),

    ADDITION(expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(ADD_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS)),

    ADDITION_EXTRA_OP(expression()
            .one(ADD_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS)),

    MULTIPLICATION(expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(MULTIPLY_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS)),

    MULTIPLICATION_EXTRA_OP(expression()
            .one(MULTIPLY_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS)),

    //Needs further check
    IF_THEN_ELSE(expression()
            .one(IF_DEF)
            .one(RAW_STATEMENT)
            .one(THEN_SECTION)
            .one(RAW_STATEMENT)
            .maybeCarousel(ELSE_SECTION, RAW_STATEMENT)
            .one(END_IF)),

    //Needs further check
    FOR_LOOP(expression()
            .one(FOR_LOOP_DEF)
            .one(START_ARGS)
            .one(RAW_STATEMENT)
            .one(END_STATEMENT)
            .one(RAW_STATEMENT)
            .one(END_STATEMENT)
            .one(END_ARGS)
            .one(RAW_STATEMENT)),

    WHILE_LOOP(expression()
            .one(WHILE_LOOP_DEF)
            .one(RAW_STATEMENT)
            .one(END_WHILE_LOOP)),

    INPUT(expression()
            .one(INPUT_DEF)
            .one(START_ARGS)
            .one(IDENTIFIER)
            .maybeMany(IDENTIFIER)
            .one(END_ARGS)),

    OUTPUT(expression()
            .one(OUTPUT_DEF)
            .one(START_ARGS)
            .one(RAW_STATEMENT)
            .one(END_ARGS)),
    ;

    private static final Stream<Formation> ALL = Arrays.stream(values());
    private static final List<Formation> ALL_LIST = Arrays.asList(values());
    private final Expression<LexType> expression;
    //private final BiConsumer<List<LexUnit>, RuntimeContext> handler

    Formation(Expression<LexType> expression) {
        this.expression = expression;
    }


    public static Stream<Formation> all() {
        return ALL;
    }

    public Expression<LexType> getExpression() {
        return expression;
    }

    public static List<Formation> allAsList() {
        return ALL_LIST;
    }

}
