package edu.psuti.alexandrov.interpret;

public class IntContainer extends Container<Integer> {

    @Override
    Integer parseValue(String newValue) {
        String lowered = newValue.toLowerCase();
        int lastIndex = lowered.length() - 1;
        char radixChar = lowered.charAt(lastIndex);
        return switch(radixChar) {
            case 'b' -> Integer.parseInt(lowered.substring(0, lastIndex), 2);
            case 'o' -> Integer.parseInt(lowered.substring(0, lastIndex), 8);
            case 'd' -> Integer.parseInt(lowered.substring(0, lastIndex));
            case 'h' -> Integer.parseInt(lowered.substring(0, lastIndex), 16);
            default -> Integer.parseInt(lowered);
        };
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

    @Override
    String nonNullRepresentaion() {
        return (value < 0 ? "1_" : "0_") + Integer.toBinaryString(value);
    }
}
