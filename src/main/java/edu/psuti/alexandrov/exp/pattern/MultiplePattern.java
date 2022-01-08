package edu.psuti.alexandrov.exp.pattern;

import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public class MultiplePattern<T> extends WalkPattern<T> {

    MultiplePattern(Predicate<T> predicate) {
        super(predicate);
    }

    @Override
    public Prediction walk(int index, List<T> values) {
        int initial = index;
        while(canStep(index, values)) {
            index++;
        }
        boolean matched = index > initial;
        return new Prediction(matched ? index : initial + 1, matched);
    }
}
