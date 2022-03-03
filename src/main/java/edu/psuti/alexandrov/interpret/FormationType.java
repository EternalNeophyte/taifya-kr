package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.exp.MatchingItem;
import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;
import edu.psuti.alexandrov.ui.ReleaseKeyListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;

import static edu.psuti.alexandrov.interpret.BiAction.emptyAction;
import static edu.psuti.alexandrov.lex.LexType.*;
import static java.util.Objects.nonNull;

public enum FormationType implements SubFormations {

    INCORRECT("Неправильная лексическая конструкция", null, null, null),

    COMMENT("Комментарий", LexType.expression().many(COMMENT_BODY), NO_CONSTRAINTS, emptyAction()),

    END("Объявление конца программы", LexType.expression().one(END_PROGRAM), NO_CONSTRAINTS, emptyAction()),

    VAR_DEF("Объявление переменной",
            LexType.expression()
            .one(IDENTIFIER)
            .maybeCarousel(LISTING, IDENTIFIER)
            .one(DELIMITER)
            .one(TYPE_DEF)
            .one(END_STATEMENT),

            NO_CONSTRAINTS,

            (formation, context) -> {
                var variables = context.variables();
                String typeDef = formation.firstUnitOfTypeOrThrow(TYPE_DEF).toString();
                Consumer<MatchResult> putVariableFunc = switch (typeDef) {
                    case "integer" -> result -> variables.put(result.group(), new IntContainer());
                    case "real" -> result -> variables.put(result.group(), new RealContainer());
                    case "boolean" -> result -> variables.put(result.group(), new BooleanContainer());
                    default -> throw new IllegalArgumentException("Недопустимый тип переменной: " + typeDef);
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

    /*VAR_ASSIGN_ARITHMETIC_EXP("Оператор присваивания результата арифметического выражения",
            LexType.expression()
            .maybeOne(ASSIGN_DEF)
            .one(IDENTIFIER)
            .one(ASSIGN_OP)
            .many(LEX_TYPE_SAMPLER.merge(OPERAND, ARITHMETIC_OP, ARGS_DEF)),

            (formation, context) -> {

            }),*/

    VAR_ASSIGN_VALUE("Операция присваивания",
            LexType.expression()
            .maybeOne(ASSIGN_DEF)
            .one(IDENTIFIER)
            .one(ASSIGN_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND),

            NO_CONSTRAINTS,

            (formation, context) -> {
                LexUnit id = formation.firstUnitOfTypeOrThrow(IDENTIFIER);
                String name = id.toString();
                Container<?> container = context.variables().get(name);
                if(nonNull(container)) {
                    context.bindArithmeticOp(context.formations().indexOf(formation));
                    context.rearrangeArithmeticOp();
                    //ToDo логику по вычислению и присвоению значений
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

            NO_CONSTRAINTS,

            ((formation, context) -> {
                LexUnit left = formation.orderedUnitOfTypeOrThrow(0, OPERAND);
                LexUnit right = formation.orderedUnitOfTypeOrThrow(1, OPERAND);
                //...
            })),


    /*COMPARISION_EXTRA_OP("Оператор сравнения",
            LexType.expression()
            .one(ADD_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),*/


    /*ADDITION("Оператор сложения",
            LexType.expression()
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .one(ADD_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),*/


    ARITHMETIC_EXP_PART("Часть арифметического выражения",
            LexType.expression()
            .one(ARITHMETIC_OP)
                    .maybeOne(START_ARGS)
            .one(OPERAND),

            NO_CONSTRAINTS,

            (formation, context) -> context.rearrangeArithmeticOp()
    ),

    ARITHMETIC_END_ARGS("Конец группировки арифметического выражения",
            LexType.expression().one(END_ARGS),

            Set.of(ARITHMETIC_EXP_PART),

            (formation, context) -> context.rearrangeArithmeticOp()
    ),


    //Сделать составным внутри COMPLEX_VAR_ASSIGN_VALUE
    /*MULTIPLICATION("Оператор умножения",
            LexType.expression()
            .maybeOne(START_ARGS)
            .maybeOne(OPERAND)
            .one(ARITHMETIC_OP)
            .one(OPERAND)
            .maybeOne(END_ARGS),

            (formation, context) -> {
                var formations = context.formations();
                var prevFormation = formations.get(formations.indexOf(formation) - 1);
                var units = formation.units();
                if(prevFormation.type().equals(VAR_ASSIGN_VALUE)) {
                    var unitsOfVarAssign = prevFormation.units();
                    units.add(unitsOfVarAssign.get(unitsOfVarAssign.size() - 1));
                }
                LexType first = units.get(0).type(), second = units.get(1).type();
                if(first.equals(ADD_OP) || first.equals(MULTIPLY_OP) ||
                        (first.equals(START_ARGS) && (second.equals(ADD_OP) || second.equals(MULTIPLY_OP))) ) {
                    throw new IllegalLexException("Выражение не может начинаться со знака бинарной операции", units.get(0));
                }
                String s = context.toRpnString(units);
                int i;
            }),*/


    /*MULTIPLICATION_EXTRA_OP("Дополнение к оператору умножения",
            LexType.expression()
            .one(MULTIPLY_OP)
            .maybeOne(START_ARGS)
            .one(OPERAND)
            .maybeOne(END_ARGS), emptyAction()),*/

    //Needs further check
    IF_THEN_ELSE("Оператор условного перехода",
            LexType.expression()
            .one(IF_DEF)
            .many(LEX_TYPE_SAMPLER.exclude(ANYTHING, IF_DEF, THEN_SECTION, END_IF))
            .one(THEN_SECTION)
            .many(LEX_TYPE_SAMPLER.exclude(ANYTHING, IF_DEF, THEN_SECTION, END_IF))
            .one(END_IF), NO_CONSTRAINTS, emptyAction()),

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

            NO_CONSTRAINTS,

            (formation, context) -> {
                var l = formation.unitsBetween(START_ARGS, END_ARGS);
                var buffer = new LinkedList<>();
                //LexAnalyzer.findFormation()
            }),

    WHILE_LOOP("Условный оператор цикла",
            LexType.expression()
            .one(WHILE_LOOP_DEF)
            .maybeMany(ANYTHING)
            .one(END_WHILE_LOOP), NO_CONSTRAINTS, emptyAction()),

    INPUT("Оператор ввода",
            LexType.expression()
            .one(INPUT_DEF)
            .one(START_ARGS)
            .one(IDENTIFIER)
            .maybeMany(IDENTIFIER)
            .one(END_ARGS),

            NO_CONSTRAINTS,

            (formation, context) -> {
                StringBuilder sb = new StringBuilder();
                context.uiHandlers()
                        .add(ui -> {
                                JTextPane pane = ui.outputPane();
                                ui.writeColoredText(pane, "Введите значение и нажмите 'Enter': ", SKY_BLUE);

                                //ToDo реализовать ожидание программы при инпуте
                                ReleaseKeyListener listener = e -> {
                                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                                        String input = pane.getText();
                                        sb.append(input.substring(input.lastIndexOf(" ")));
                                        formation.unitsBetween(START_ARGS, END_ARGS)
                                                .forEach(id -> context.optionalOfVar(id)
                                                        .ifPresent(found -> found.put(sb.toString()))
                                                );
                                    }
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

            NO_CONSTRAINTS,

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
    private final Set<FormationType> advanceConstraints;
    private final BiAction<Formation, RuntimeContext> action;

    FormationType(String description, Expression<LexType> expression, Set<FormationType> advanceConstraints, BiAction<Formation, RuntimeContext> action) {
        this.description = description;
        this.expression = expression;
        this.advanceConstraints = advanceConstraints;
        this.action = action;
    }

    public static Optional<MatchingItem<FormationType>> atLeastOne(List<LexType> lexTypes) {
        return Arrays.stream(values())
                .skip(1)
                .map(type -> new MatchingItem<>(type.expression.compute(lexTypes), type))
                .sorted()
                .findFirst();
    }

    public Expression<LexType> expression() {
        return expression;
    }

    public BiAction<Formation, RuntimeContext> action() {
        return action;
    }

    public Set<FormationType> advanceConstraints() {
        return advanceConstraints;
    }

    @Override
    public String toString() {
        return description;
    }
}
