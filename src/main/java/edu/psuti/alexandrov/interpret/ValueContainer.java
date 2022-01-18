package edu.psuti.alexandrov.interpret;

import java.util.concurrent.atomic.AtomicReference;

public record ValueContainer<T>(AtomicReference<T> reference, int scopeStart, int scopeEnd) {

    public void put(T newValue) {
        reference.set(newValue);
    }


}
