package edu.psuti.alexandrov.interpret;

public class BooleanContainer extends Container<Boolean> {

    @Override
    Boolean parseValue(String newValue) {
        return Boolean.parseBoolean(newValue);
    }

    @Override
    Boolean executeComputing(String opDef, Boolean operand) {
        return switch (opDef) {
            case "or" -> value || operand;
            case "and" -> value && operand;
            default -> throw new IllegalArgumentException("Недопустимая операция вычисления '" +
                                                            opDef + "' для типа boolean");
        };
    }

    @Override
    boolean executeComparing(String opDef, Boolean operand) {
        return switch (opDef) {
            case "==" -> value == operand;
            case "!=" -> value != operand;
            default -> throw new IllegalArgumentException("Недопустимая операция сравнения '" +
                                                            opDef + "' для типа boolean");
        };
    }
}
