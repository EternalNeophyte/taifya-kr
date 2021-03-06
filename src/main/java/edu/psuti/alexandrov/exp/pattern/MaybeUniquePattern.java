package edu.psuti.alexandrov.exp.pattern;


import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public class MaybeUniquePattern<T> extends WalkPattern<T> {

    public MaybeUniquePattern(Predicate<T> predicate) {
        super(predicate);
    }

    @Override
    public Prediction walk(int index, List<T> values) {
        return new Prediction(canStep(index, values) ? index + 1 : index, true);
    }
}
