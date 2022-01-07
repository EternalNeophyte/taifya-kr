package edu.psuti.alexandrov.exp.pattern;

import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public class UniquePattern<T> extends WalkPattern<T> {

    UniquePattern(Predicate<T> predicate) {
        super(predicate);
    }

    @Override
    Prediction retrieve(int index, List<T> values) {
        System.out.println("Unique index = " + index + ", value = " + values.get(index));
        return new Prediction(index + 1, test(index, values));
    }
}
