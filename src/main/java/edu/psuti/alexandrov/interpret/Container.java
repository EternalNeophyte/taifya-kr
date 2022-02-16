package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;

import static edu.psuti.alexandrov.lex.LexType.*;

public abstract class Container<T> {

    T value;

    public T value() {
        return value;
    }

    public void put(LexUnit newValue) {
        try {
            value = parseValue(newValue.result().group());
        }
        catch (Throwable e) {
            throw new IllegalLexException("Тип присваемого значения не соответствует " +
                                            "типу переменной", newValue);
        }
    }

    public final T compute(LexUnit op, T operand) {
        String opDef = op.result().group();
        LexType opType = op.type();
        if(notEquals(opType, ADD_OP) && notEquals(opType, MULTIPLY_OP)) {
            throw new IllegalLexException("Ожидалась операция вычисления вместо " + op.type(), op);
        }
        return executeComputing(opDef, operand);
    }

    public final boolean compare(LexUnit op, T operand) {
        String opDef = op.result().group();
        if(notEquals(op.type(), COMPARE_OP)) {
            throw new IllegalLexException("Ожидалась операция сравнения вместо " + op.type(), op);
        }
        return executeComparing(opDef, operand);
    }

    abstract T parseValue(String newValue);
    abstract T executeComputing(String opDef, T operand);
    abstract boolean executeComparing(String opDef, T operand);

}
