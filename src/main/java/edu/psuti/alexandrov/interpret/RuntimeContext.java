package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexUnit;
import edu.psuti.alexandrov.ui.UIFrame;
import edu.psuti.alexandrov.util.BiBuffer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static edu.psuti.alexandrov.interpret.FormationType.VAR_ASSIGN_VALUE;
import static edu.psuti.alexandrov.lex.LexType.START_ARGS;
import static java.util.Objects.isNull;

/**
 * Created on 17.01.2022 by
 *
 * @author alexandrov
 */
public record RuntimeContext
        (Map<String, Container<?>> variables,
         List<Formation> formations,
         List<Consumer<UIFrame>> uiHandlers,
         BiBuffer<LexUnit, String> errors,
         LinkedList<ArithmeticOpPosition> opPositions,
         int[] wrapPositions)
{

    public RuntimeContext(List<Formation> formations, BiBuffer<LexUnit, String> errors, int[] wrapPositions) {
        this(new HashMap<>(), formations, new LinkedList<>(), errors, new LinkedList<>(), wrapPositions);
    }

    public static record LexPosition(int line, int column) { }

    public LexPosition computePosition(LexUnit unit) {
        int line = 0, flatPos = unit.result().start();
        while (line < wrapPositions.length && wrapPositions[line] < flatPos) {
            line++;
        }
        int column = line > 0 ? flatPos - wrapPositions[line - 1] : flatPos;
        return new LexPosition(line + 1, column + 1);
    }

    public static record ArithmeticOpPosition(int start, AtomicInteger end) { }

    public void bindArithmeticOp(int start) {
        opPositions.add(new ArithmeticOpPosition(start, new AtomicInteger(start)));
    }

    public void rearrangeArithmeticOp() {
        opPositions.getLast().end().incrementAndGet();
    }

    public boolean hasEnd() {
        return !formations.isEmpty() && formations.get(formations.size() - 1)
                                                    .type().equals(FormationType.END);
    }

    public boolean runWithNoErrors() {
        if(!hasEnd()) {
            errors.put(null, "Не найден 'end' в конце программы");
        }
        if(errors.isEmpty()) {
            try {
                formations.forEach(formation -> formation.deployIn(this));
            }
            catch (IllegalLexException e) {
                errors.put(e.unit(), e.getMessage());
            }
            catch (Throwable e) {
                errors.put(null, e.getMessage());
            }
        }
        return errors.isEmpty();
    }

    //Reverse Polish notation, RPN
    public void forEachRpn(Consumer<String> consumer) {
        opPositions.forEach(pos -> {
            String notation = toRpnString(formations.subList(pos.start, pos.end.get()));
            consumer.accept(notation);
        });
    }

    public Optional<Container<?>> optionalOfVar(LexUnit varDefinition) {
        String varName = varDefinition.toString();
        return Optional.ofNullable(variables.get(varName));
    }

    public static final Comparator<LexUnit> OPS_BY_PRIORITY = Comparator.comparingInt(RuntimeContext::priorityOfOp);

    public static int priorityOfOp(LexUnit opSignHolder) {
        String opSign = opSignHolder.toString();
        return switch (opSign) {
            case "or", "plus", "minus" -> 1;
            case "and", "*", "/" -> 2;
            case "(", ")" -> 3;
            case "!" -> 4;
            default -> throw new IllegalArgumentException(opSignHolder + " не является операцией");
        };
    }

    public String toRpnString(List<Formation> formations) {
        StringJoiner rpn = new StringJoiner(" ");
        //"Операнды в двоичном представлении"
        StringJoiner binaries = new StringJoiner("\n\t");
        Stack<LexUnit> stack = new Stack<>();
        for(Formation formation : formations) {
            var units = formation.units();
            for(LexUnit unit : formation.type().equals(VAR_ASSIGN_VALUE)
                                    ? units.subList(2, units.size())
                                    : units) {
                switch(unit.type()) {
                    case BINARY_NUM, OCTET_NUM, DECIMAL_NUM, HEX_NUM, FLOAT_NUM -> {
                        String value = unit.toString();
                        rpn.add(value);
                        binaries.add(value);
                    }
                    case IDENTIFIER -> optionalOfVar(unit)
                            .map(c -> {
                                if(c instanceof BooleanContainer) {
                                    throw new IllegalLexException("Переменная '" + unit +
                                            "' типа boolean не может быть частью арифметического выражения", unit);
                                }
                                if(isNull(c.value)) {
                                    throw new IllegalLexException("Переменной '" + unit +
                                            "' еще не было присвоено значение", unit);
                                }
                                return c.value.toString();
                            })
                            .ifPresentOrElse(rpn::add, () -> {
                                throw new IllegalLexException("Переменная '" + unit +
                                            "' еще не была объявлена", unit);
                            });
                    case START_ARGS -> stack.push(unit);
                    case END_ARGS -> {
                        while (!stack.empty() && !stack.peek().type().equals(START_ARGS)) {
                            rpn.add(stack.pop().toString());
                        }
                        if(!stack.empty()) {
                            stack.pop();
                        }
                    }
                    case ADD_OP, MULTIPLY_OP, UNARY_OP -> {
                        while (!stack.empty() && OPS_BY_PRIORITY.compare(stack.peek(), unit) <= 0) {
                            LexUnit popped = stack.pop();
                            if(!popped.type().equals(START_ARGS)) {
                                rpn.add(popped.toString());
                            }
                        }
                        stack.push(unit);
                    }
                    default -> throw new IllegalLexException(unit.type() +
                            " не ожидается здесь [Перевод в постфиксную форму]", unit);
                }
            }
        }
        stack.forEach(unit -> rpn.add(unit.toString()));
        return rpn.toString();
    }

    public String toBinaryString(LexUnit unit) {
        //ToDo
        return switch (unit.type()) {
            case FLOAT_NUM -> "";
            case BINARY_NUM, OCTET_NUM, DECIMAL_NUM, HEX_NUM -> "";
            default -> "";
        };
    }

}
