package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.exp.Matching;
import edu.psuti.alexandrov.lex.LexType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static edu.psuti.alexandrov.lex.LexType.*;

public enum FormationType {

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
            .one(END_ARGS))
    ;


    private final Expression<LexType> expression;


    FormationType(Expression<LexType> expression) {
        this.expression = expression;
    }


    public static Optional<FormationType> findCompleteMatching(List<LexType> lexTypes) {
        for(FormationType type : values()) {
            Matching matching = type.expression.compute(lexTypes);
            if(matching.isComplete()) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public static Stream<FormationType> all() {
        return Arrays.stream(values());
    }

}
