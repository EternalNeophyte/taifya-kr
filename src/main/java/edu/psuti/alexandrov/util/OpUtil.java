package edu.psuti.alexandrov.util;

import edu.psuti.alexandrov.interpret.BooleanContainer;
import edu.psuti.alexandrov.interpret.Formation;
import edu.psuti.alexandrov.interpret.RuntimeContext;
import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexUnit;

import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.StringJoiner;

import static edu.psuti.alexandrov.interpret.FormationType.VAR_ASSIGN_VALUE;
import static edu.psuti.alexandrov.lex.LexType.START_ARGS;
import static java.util.Objects.isNull;

public final class OpUtil {

    public static final Comparator<LexUnit> OPS_BY_PRIORITY = Comparator.comparingInt(OpUtil::priorityOfOp);

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

    private static double executeOp(String opSign, double first, double second) {
        return switch (opSign) {
            case "or" -> (int) first | (int) second;
            case "plus" -> first + second;
            case "minus" -> first - second;
            case "and" -> (int) first & (int) second;
            case "*" -> first * second;
            case "/" -> first == 0d || second == 0d ? 0d : first / second;
            default -> 0d;
        };
    }

    public static double calculate(String rpn) {
        Stack<Double> currencies = new Stack<>();
        for(String part : rpn.split(" ")) {
            if(part.equals("!")) {
                int last = currencies.empty() ? 0 : currencies.pop().intValue();
                currencies.push((double) ~last);
            }
            else {
                double first = currencies.empty() ? 0d : currencies.pop(),
                        second = currencies.empty() ? 0d : currencies.pop();
                currencies.push(executeOp(part, first, second));
            }
        }
        return currencies.pop();
    }

    public static String toRpnString(RuntimeContext context, List<Formation> formations) {
        StringJoiner rpn = new StringJoiner(" ");
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
                        binaries.add(toBinaryString(unit));
                    }
                    case IDENTIFIER -> context.optionalOfVar(unit)
                            .map(c -> {
                                if(c instanceof BooleanContainer) {
                                    throw new IllegalLexException("Переменная '" + unit +
                                            "' типа boolean не может быть частью арифметического выражения", unit);
                                }
                                if(isNull(c.value())) {
                                    throw new IllegalLexException("Переменной '" + unit +
                                            "' еще не было присвоено значение", unit);
                                }
                                binaries.add(c.toString());
                                return c.value().toString();
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
        return rpn + "\nОперанды в двоичном представлении\n\t" + binaries;
    }

    public static String toBinaryString(LexUnit unit) {
        String raw = unit.result().group().toLowerCase();
        int lastChar = raw.length() - 1;
        int number = switch (unit.type()) {
            case FLOAT_NUM -> Double.valueOf(raw).intValue();
            case BINARY_NUM -> Integer.parseInt(raw.substring(0, lastChar), 2);
            case OCTET_NUM -> Integer.parseInt(raw.substring(0, lastChar), 8);
            case DECIMAL_NUM -> Integer.parseInt(raw.endsWith("d")
                    ? raw.substring(0, lastChar)
                    : raw);
            case HEX_NUM -> Integer.parseInt(raw.substring(0, lastChar), 16);
            default -> throw new IllegalLexException("'" + unit +
                    "' невозможно представить в виде числа", unit);
        };
        return toSignedBinaryString(number);
    }

    public static String toSignedBinaryString(int number) {
        return number >= 0
                ? "0_" + Integer.toBinaryString(number)
                : "1_" + Integer.toBinaryString(-number);
    }

}
