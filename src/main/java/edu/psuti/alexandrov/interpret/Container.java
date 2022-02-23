package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;

import static edu.psuti.alexandrov.lex.LexType.*;
import static java.util.Objects.isNull;

public abstract class Container<T> {

    T value;

    public T value() {
        return value;
    }


    public static Container<?> of(Class<?> klass) {
        if(klass.equals(Boolean.class) || klass.equals(boolean.class)) {
            return new BooleanContainer();
        }
        else if(klass.equals(Integer.class) || klass.equals(int.class)) {
            return new IntContainer();
        }
        else if(klass.equals(Double.class) || klass.equals(double.class)) {
            return new RealContainer();
        }
        else {
            throw new IllegalArgumentException("Ошибка интерпретатора: " +
                    "не удалось создать контейнер для переменной");
        }
    }

    public void put(String newValue) {
        try {
            value = parseValue(newValue.replaceAll("\\W", ""));
        }
        catch (Throwable e) {
            throw new IllegalArgumentException("Тип присваиваемого значения [" + newValue +
                    "] не соответствует " + "типу переменной");
        }
    }

    public void put(LexUnit newValue) {
        try {
            value = parseValue(newValue.toString());
        }
        catch (Throwable e) {
            throw new IllegalLexException("Тип присваиваемого значения не соответствует " +
                                            "типу переменной", newValue);
        }
    }

    public final T compute(LexUnit op, T operand) {
        String opDef = op.toString();
        LexType opType = op.type();
        if(notEquals(opType, ADD_OP) && notEquals(opType, MULTIPLY_OP)) {
            throw new IllegalLexException("Ожидалась операция вычисления вместо " + op.type(), op);
        }
        return executeComputing(opDef, operand);
    }

    public final boolean compare(LexUnit op, T operand) {
        String opDef = op.toString();
        if(notEquals(op.type(), COMPARE_OP)) {
            throw new IllegalLexException("Ожидалась операция сравнения вместо " + op.type(), op);
        }
        return executeComparing(opDef, operand);
    }

    abstract T parseValue(String newValue);
    abstract T executeComputing(String opDef, T operand);
    abstract boolean executeComparing(String opDef, T operand);
    abstract String nonNullRepresentaion();

    @Override
    public String toString() {
        return "Значение [" + (isNull(value) ? "нет" : nonNullRepresentaion()) + "]";
    }
}
