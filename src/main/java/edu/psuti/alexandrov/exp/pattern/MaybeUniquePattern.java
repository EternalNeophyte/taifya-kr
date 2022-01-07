package edu.psuti.alexandrov.exp.pattern;


import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public class MaybeUniquePattern<T> extends WalkPattern<T> {

    public MaybeUniquePattern(Predicate<T> predicate) {
        super(predicate);
    }

    @Override
    Prediction retrieve(int index, List<T> values) {
        System.out.println("Maybe uniqye index = " + index + ", value = " + values.get(index));
        return new Prediction(test(index, values) ? index + 1 : index, true);
    }
}
