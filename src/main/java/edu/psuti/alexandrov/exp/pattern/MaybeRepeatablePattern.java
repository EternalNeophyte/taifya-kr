package edu.psuti.alexandrov.exp.pattern;

import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public class MaybeRepeatablePattern<T> extends WalkPattern<T> {

    MaybeRepeatablePattern(Predicate<T> predicate) {
        super(predicate);
    }

    @Override
    Prediction retrieve(int index, List<T> values) {
        while(test(index, values)) {
            System.out.println("Maybe repeatable index = " + index + ", value = " + values.get(index));
            index++;
        }
        return new Prediction(index, true);
    }
}
