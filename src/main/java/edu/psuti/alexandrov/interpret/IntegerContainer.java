package edu.psuti.alexandrov.interpret;

public class IntegerContainer extends Container<Integer> {

    @Override
    Integer parseValue(String newValue) {
        return Integer.parseInt(newValue);
    }

    @Override
    Integer executeComputing(String opDef, Integer operand) {
        return switch (opDef) {
            case "or" -> value | operand;
            case "plus" -> value + operand;
            case "minus" -> value - operand;
            case "and" -> value & operand;
            case "*" -> value * operand;
            case "/" -> value / operand;
            default -> 0;
        };
    }

    @Override
    boolean executeComparing(String opDef, Integer operand) {
        return switch (opDef) {
            case "==" -> value.intValue() == operand.intValue();
            case "!=" -> value.intValue() != operand.intValue();
            case ">=" -> value >= operand;
            case "<=" -> value <= operand;
            case ">" -> value > operand;
            case "<" -> value < operand;
            default -> false;
        };
    }
}
