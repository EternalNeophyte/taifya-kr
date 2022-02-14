package edu.psuti.alexandrov;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toUnmodifiableMap;

public class Calculator {

    private enum Operation {

        ADD(3, "+", Double::sum),
        SUBTRACT(3, "-", (op1, op2) -> op1 - op2),
        MULTIPLY(2, "*", (op1, op2) -> op1 * op2),
        DIVIDE(2, "/", (op1, op2) -> op1 / op2),
        TAKE_REMINDER(2, "%", (op1, op2) -> op1 % op2),
        FLIP_BITS(0, "~", (op1, op2) -> (double) ~op1.intValue()),
        MOVE_BITS_LEFT(1, "<<", (op1, op2) -> (double) (op1.intValue() << op2.intValue())),
        MOVE_BITS_RIGHT(1, ">>", (op1, op2) -> (double) (op1.intValue() >> op2.intValue()))
        ;

        public static final Comparator<Operation> BY_ORDER = Comparator.comparingInt(op -> op.order);
        public static final Map<String, Operation> CASHE = Arrays
                .stream(values())
                .collect(toUnmodifiableMap(op -> op.sign,
                                            Function.identity()));

        private final int order;
        private final String sign;
        private final BinaryOperator<Double> executor;

        Operation(int order, String sign, BinaryOperator<Double> executor) {
            this.order = order;
            this.sign = sign;
            this.executor = executor;
        }

        public double execute(double op1, double op2) {
            return executor.apply(op1, op2);
        }
    }

    public static void main(String... args) {
        final String number = "(\\d+\\.)?\\d+";
        final String sign = "([-+*/%~^]|<<|>>)";
        Pattern signPattern = compile("[-+*/%~^]|<<|>>"),
                opPattern = compile("(\\d+\\.)?\\d+");
        Queue<Operation> signs = new PriorityQueue<>(Operation.BY_ORDER);
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n")
                .forEachRemaining(line -> {
                    fill(signs, Operation.CASHE::get, signPattern, line);
                    while(!signs.isEmpty()) {
                        Operation op = signs.poll();
                        System.out.println("Polled: " + op + " order " + op.order);
                    }
                    /*fill(signs, signPattern, line);
                    fill(operands, opPattern, line);
                    while(operands.size() > 1) {
                        String op2 = operands.pop();
                        String op1 = operands.pop();
                        double res = compute(signs.pop(), op1, op2);
                        operands.push(res + "");
                        System.out.println(res);
                    }*/
                });
    }

    private static <T> void fill(Queue<T> queue,
                                 Function<String, T> groupToQueueElement,
                                 Pattern pattern, String line) {
        pattern.matcher(line)
                .results()
                .map(MatchResult::group)
                .map(groupToQueueElement)
                .forEach(queue::add);
    }

}
