package edu.psuti.alexandrov.exp.pattern;

import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public class UniquePattern<T> extends WalkPattern<T> {

    UniquePattern(Predicate<T> predicate) {
        super(predicate);
    }

    @Override
    public Prediction walk(int index, List<T> values) {
        return new Prediction(index + 1, canStep(index, values));
    }
}
