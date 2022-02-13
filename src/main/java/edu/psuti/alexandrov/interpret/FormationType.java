package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.exp.MatchingItem;
import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.stream.Stream;

import static edu.psuti.alexandrov.interpret.BiAction.emptyAction;
import static edu.psuti.alexandrov.lex.LexType.*;

public enum FormationType implements SubFormations {

    COMMENT(LexType.expression().many(COMMENT_BODY), emptyAction()),

    END(LexType.expression().one(END_PROGRAM), emptyAction()),

    VAR_DEF(LexType.expression()
            .one(IDENTIFIER)
            .maybeCarousel(LISTING, IDENTIFIER)
            .one(DELIMITER)
            .one(TYPE_DEF)
            .one(END_STATEMENT),

            (formation, context) -> {
                var variables = context.variables();
                String typeDef = formation.firstUnitOfType(TYPE_DEF)
                        .result()
                        .group();
                Consumer<MatchResult> putVariableFunc = switch (typeDef) {
                    case "integer" -> result -> variables.put(result.group(), new IntegerContainer());
                    case "real" -> result -> variables.put(result.group(), new RealContainer());
                    case "boolean" -> result -> variables.put(result.group(), new BooleanContainer());
                    default -> throw new IllegalArgumentException("Недопустимый тип переменной");
                };
                formation.unitsListOfType(IDENTIFIER)
                        .forEach(unit -> {
                            MatchResult result = unit.result();
                            String name = result.group();
                            if(variables.containsKey(name)) {
                                throw new IllegalLexException("Переменная '" + name + " ' уже была объявлена", unit);
                            }
                            putVariableFunc.accept(result);
                        });
            }),

    VAR_ASSIGN_VALUE(LexType.expression()
            .maybeOne(ASSIGN_DEF)
            .one(IDENTIFIER)
            .one(ASSIGN_OP)
            .one(OPERAND),

            (formation, context) -> {
                LexUnit unit = formation.firstUnitOfType(IDENTIFIER);
                String name = unit.result().group();
                Container<?> container = context.variables().get(name);
                Optional.ofNullable(container)
                        .ifPresentOrElse(c -> c.put(unit), () -> {
                            throw new IllegalLexException("Переменная '" + name +
                                                " ' еще не была объявлена", unit);
                        });
            }),

    COMPARISION(LexType.expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(COMPARE_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),

    COMPARISION_EXTRA_OP(LexType.expression()
            .one(ADD_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),

    ADDITION(LexType.expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(ADD_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),

    ADDITION_EXTRA_OP(LexType.expression()
            .one(ADD_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),

    MULTIPLICATION(LexType.expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(MULTIPLY_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),

    MULTIPLICATION_EXTRA_OP(LexType.expression()
            .one(MULTIPLY_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),

    //Needs further check
    IF_THEN_ELSE(LexType.expression()
            .one(IF_DEF)
            .many(LEX_TYPE_SAMPLER.exclude(ANYTHING, IF_DEF, THEN_SECTION, END_IF))
            .one(THEN_SECTION)
            .many(LEX_TYPE_SAMPLER.exclude(ANYTHING, IF_DEF, THEN_SECTION, END_IF))
            .one(END_IF), emptyAction()),

    //Needs further check
    FOR_LOOP(LexType.expression()
            .one(FOR_LOOP_DEF)
            .one(START_ARGS)
            .maybeOne(IDENTIFIER)
            .maybeOne(ASSIGN_OP)
            .maybeMany(LEX_TYPE_SAMPLER.merge(OPERAND, ARITHMETIC_OP))
            .one(END_STATEMENT)
            .maybeOne(IDENTIFIER)
            .maybeOne(COMPARE_OP)
            .maybeMany(LEX_TYPE_SAMPLER.merge(OPERAND, ARITHMETIC_OP))
            .one(END_STATEMENT)
            .maybeOne(IDENTIFIER)
            .maybeOne(ASSIGN_OP)
            .maybeMany(LEX_TYPE_SAMPLER.merge(OPERAND, ARITHMETIC_OP))
            .one(END_ARGS), emptyAction()),

    WHILE_LOOP(LexType.expression()
            .one(WHILE_LOOP_DEF)
            .maybeMany(ANYTHING)
            .one(END_WHILE_LOOP), emptyAction()),

    INPUT(LexType.expression()
            .one(INPUT_DEF)
            .one(START_ARGS)
            .one(IDENTIFIER)
            .maybeMany(IDENTIFIER)
            .one(END_ARGS), emptyAction()),

    OUTPUT(LexType.expression()
            .one(OUTPUT_DEF)
            .one(START_ARGS)
            .maybeMany(ANYTHING)
            .one(END_ARGS), emptyAction())
    ;

    private final Expression<LexType> expression;
    private final BiAction<Formation, RuntimeContext> action;

    FormationType(Expression<LexType> expression, BiAction<Formation, RuntimeContext> action) {
        this.expression = expression;
        this.action = action;
    }

    public static Optional<MatchingItem<FormationType>> atLeastOne(List<LexType> lexTypes) {
        AtomicInteger count = new AtomicInteger();
        return Arrays.stream(values())
                .map(type -> new MatchingItem<>(type.expression.compute(lexTypes), type))
                .sorted()
                //.takeWhile(item -> count.getAndIncrement() > 0 || item.matching().isComplete())
                .findFirst();
    }


    public static Stream<FormationType> all() {
        return Arrays.stream(values());
    }

    public Expression<LexType> expression() {
        return expression;
    }

    public BiAction<Formation, RuntimeContext> action() {
        return action;
    }
}
