package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.exp.MatchingItem;
import edu.psuti.alexandrov.lex.LexType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static edu.psuti.alexandrov.lex.LexType.*;
import static edu.psuti.alexandrov.util.ArraySamples.merge;

public enum FormationType {

    COMMENT(expression().many(COMMENT_BODY)),

    END(expression().one(END_PROGRAM)),

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
            .maybeMany(ANYTHING)
            .one(THEN_SECTION)
            .maybeMany(ANYTHING)
            .one(END_IF)),

    //Needs further check
    FOR_LOOP(expression()
            .one(FOR_LOOP_DEF)
            .one(START_ARGS)
            .maybeOne(IDENTIFIER)
            .maybeOne(ASSIGN_OP)
            .maybeMany(merge(LexType[]::new, OPERAND, ARITHMETIC_OP))
            .one(END_STATEMENT)
            .maybeOne(IDENTIFIER)
            .maybeOne(COMPARE_OP)
            .maybeMany(merge(LexType[]::new, OPERAND, ARITHMETIC_OP))
            .one(END_STATEMENT)
            .maybeOne(IDENTIFIER)
            .maybeOne(ASSIGN_OP)
            .maybeMany(merge(LexType[]::new, OPERAND, ARITHMETIC_OP))
            .one(END_ARGS)),

    WHILE_LOOP(expression()
            .one(WHILE_LOOP_DEF)
            .maybeMany(ANYTHING)
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
            .maybeMany(ANYTHING)
            .one(END_ARGS))
    ;

    private final Expression<LexType> expression;


    FormationType(Expression<LexType> expression) {
        this.expression = expression;
    }

    public static MatchingItem<FormationType> findFirst(List<LexType> lexTypes) {
        return Arrays.stream(values())
                .map(type -> new MatchingItem<>(type.expression.compute(lexTypes), type))
                .sorted()
                .findFirst()
                .orElseThrow();
    }


    public static Stream<FormationType> all() {
        return Arrays.stream(values());
    }

}
