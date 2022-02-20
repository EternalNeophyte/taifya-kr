package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.exp.MatchingItem;
import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;
import edu.psuti.alexandrov.ui.ReleaseKeyListener;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.psuti.alexandrov.interpret.BiAction.emptyAction;
import static edu.psuti.alexandrov.lex.LexType.*;
import static java.util.Objects.nonNull;

public enum FormationType implements SubFormations {

    INCORRECT("Неправильная лексическая конструкция", null, null),

    COMMENT("Комментарий", LexType.expression().many(COMMENT_BODY), emptyAction()),

    END("Объявление конца программы", LexType.expression().one(END_PROGRAM), emptyAction()),

    VAR_DEF("Объявление переменной",
            LexType.expression()
            .one(IDENTIFIER)
            .maybeCarousel(LISTING, IDENTIFIER)
            .one(DELIMITER)
            .one(TYPE_DEF)
            .one(END_STATEMENT),

            (formation, context) -> {
                var variables = context.variables();
                String typeDef = formation.firstUnitOfTypeOrThrow(TYPE_DEF).toString();
                Consumer<MatchResult> putVariableFunc = switch (typeDef) {
                    case "integer" -> result -> variables.put(result.group(), new IntContainer());
                    case "real" -> result -> variables.put(result.group(), new RealContainer());
                    case "boolean" -> result -> variables.put(result.group(), new BooleanContainer());
                    default -> throw new IllegalArgumentException("Недопустимый тип переменной");
                };
                formation.unitsOfType(IDENTIFIER)
                        .forEach(unit -> {
                            MatchResult result = unit.result();
                            String name = result.group();
                            if(variables.containsKey(name)) {
                                throw new IllegalLexException("Переменная '" + name + " ' уже была объявлена", unit);
                            }
                            putVariableFunc.accept(result);
                        });
            }),


    VAR_ASSIGN_VALUE("Операция присваивания",
            LexType.expression()
            .maybeOne(ASSIGN_DEF)
            .one(IDENTIFIER)
            .one(ASSIGN_OP)
            .one(OPERAND),

            (formation, context) -> {
                LexUnit id = formation.firstUnitOfTypeOrThrow(IDENTIFIER);
                String name = id.toString();
                Container<?> container = context.variables().get(name);
                if(nonNull(container)) {
                    container.put(formation.firstUnitOfTypeOrThrow(RIGHT_VALUE));
                }
                else {
                    throw new IllegalLexException("Переменная '" + name +
                                                "' еще не была объявлена", id);
                }
            }),


    COMPARISION("Операция сравнения",
            LexType.expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(COMPARE_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS),

            ((formation, context) -> {
                LexUnit left = formation.orderedUnitOfTypeOrThrow(0, OPERAND);
                LexUnit right = formation.orderedUnitOfTypeOrThrow(1, OPERAND);
                //...
            })),


    COMPARISION_EXTRA_OP("Оператор сравнения",
            LexType.expression()
            .one(ADD_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),


    ADDITION("Оператор сложения",
            LexType.expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(ADD_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),


    ADDITION_EXTRA_OP("Дополнение к оператору сложения",
            LexType.expression()
            .one(ADD_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),


    MULTIPLICATION("Оператор умножения",
            LexType.expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(MULTIPLY_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),


    MULTIPLICATION_EXTRA_OP("Дополнение к оператору умножения",
            LexType.expression()
            .one(MULTIPLY_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),

    //Needs further check
    IF_THEN_ELSE("Оператор условного перехода",
            LexType.expression()
            .one(IF_DEF)
            .many(LEX_TYPE_SAMPLER.exclude(ANYTHING, IF_DEF, THEN_SECTION, END_IF))
            .one(THEN_SECTION)
            .many(LEX_TYPE_SAMPLER.exclude(ANYTHING, IF_DEF, THEN_SECTION, END_IF))
            .one(END_IF), emptyAction()),

    //Needs further check
    FOR_LOOP("Оператор цикла с фиксированным числом повторений",
            LexType.expression()
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
            .one(END_ARGS),

            (formation, context) -> {
                var l = formation.unitsBetween(START_ARGS, END_ARGS);
                var buffer = new LinkedList<>();
                //LexAnalyzer.findFormation()
            }),

    WHILE_LOOP("Условный оператор цикла",
            LexType.expression()
            .one(WHILE_LOOP_DEF)
            .maybeMany(ANYTHING)
            .one(END_WHILE_LOOP), emptyAction()),

    INPUT("Оператор ввода",
            LexType.expression()
            .one(INPUT_DEF)
            .one(START_ARGS)
            .one(IDENTIFIER)
            .maybeMany(IDENTIFIER)
            .one(END_ARGS),

            (formation, context) -> {
                StringBuilder sb = new StringBuilder();
                context.uiHandlers()
                        .add(ui -> {
                                JTextPane pane = ui.outputPane();
                                ui.writeColoredText(pane, "Введите значение и нажмите 'Enter': ", SKY_BLUE);

                                //ToDo реализовать ожидание программы при инпуте
                                ReleaseKeyListener listener = e -> {
                                    String input = pane.getText();
                                    sb.append(input.substring(input.lastIndexOf(" ")));
                                    formation.unitsBetween(START_ARGS, END_ARGS)
                                            .forEach(id -> context.optionalOfVar(id)
                                                    .ifPresent(found -> found.put(sb.toString()))
                                            );
                                };
                                pane.addKeyListener(listener);

                        });

            }),

    OUTPUT("Оператор вывода",
            LexType.expression()
            .one(OUTPUT_DEF)
            .one(START_ARGS)
            .maybeMany(LEX_TYPE_SAMPLER.exclude(ANYTHING, END_ARGS))
            .one(END_ARGS),

            (formation, context) -> {
                String output = formation.unitsBetween(START_ARGS, END_ARGS)
                        .stream()
                        .map(unit -> context.optionalOfVar(unit)
                                        .map(Container::value)
                                        .map(Object::toString)
                                        .orElse(unit.toString()))
                        .collect(Collectors.joining(" ", "", "\n"));
                context.uiHandlers()
                        .add(ui -> ui.writeColoredText(ui.outputPane(), output, SAKURA_SNOW));
            })
    ;

    private final String description;
    private final Expression<LexType> expression;
    private final BiAction<Formation, RuntimeContext> action;

    FormationType(String description, Expression<LexType> expression, BiAction<Formation, RuntimeContext> action) {
        this.description = description;
        this.expression = expression;
        this.action = action;
    }

    public static Optional<MatchingItem<FormationType>> atLeastOne(List<LexType> lexTypes) {
        return Arrays.stream(values())
                .skip(1)
                .map(type -> new MatchingItem<>(type.expression.compute(lexTypes), type))
                .sorted()
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

    @Override
    public String toString() {
        return description;
    }
}
