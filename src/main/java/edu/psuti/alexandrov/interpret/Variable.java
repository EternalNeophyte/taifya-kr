package edu.psuti.alexandrov.interpret;

import java.util.concurrent.atomic.AtomicReference;

public record Variable<T>(String name, AtomicReference<T> valueHolder, int scopeStart, int scopeEnd) {

    public void assign(T newValue) {
        valueHolder.set(newValue);
    }
}
