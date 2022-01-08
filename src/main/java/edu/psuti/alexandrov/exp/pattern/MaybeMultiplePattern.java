package edu.psuti.alexandrov.exp.pattern;

import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public class MaybeMultiplePattern<T> extends WalkPattern<T> {

    MaybeMultiplePattern(Predicate<T> predicate) {
        super(predicate);
    }

    @Override
    public Prediction walk(int index, List<T> values) {
        while(canStep(index, values)) {
            index++;
        }
        return new Prediction(index, true);
    }
}
