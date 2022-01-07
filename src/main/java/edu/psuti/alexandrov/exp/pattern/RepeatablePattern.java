package edu.psuti.alexandrov.exp.pattern;

import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public class RepeatablePattern<T> extends WalkPattern<T> {

    RepeatablePattern(Predicate<T> predicate) {
        super(predicate);
    }

    @Override
    Prediction retrieve(int index, List<T> values) {
        int initial = index;
        while(test(index, values)) {
            System.out.println("Maybe repeatable index = " + index + ", value = " + values.get(index));
            index++;
        }
        boolean result = index > initial;
        return new Prediction(result ? index : initial + 1, result);
    }
}
