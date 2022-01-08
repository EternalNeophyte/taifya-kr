package edu.psuti.alexandrov.exp.pattern;

import edu.psuti.alexandrov.exp.Prediction;

import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractRepeatablePattern<T> extends WalkPattern<T> {

    final T[] row;

    AbstractRepeatablePattern(Predicate<T> predicate, T[] row) {
        super(predicate);
        this.row = row;
    }

    abstract boolean mergeConditions(boolean main, boolean additional);

    @Override
    public Prediction walk(int index, List<T> values) {
        int rowLength = row.length,
            initial = index,
            shift = index < rowLength ? index : index % rowLength;
        boolean allMatched = true;

        while(canStep(index, values)) {
            T comparable = row[index < rowLength
                                    ? index - shift
                                    : (index - shift) % rowLength];
            if(!values.get(index).equals(comparable)) {
                allMatched = false;
            }
            index++;
        }

        return new Prediction(index - ((index - shift) % rowLength),
                            mergeConditions(allMatched, (index - initial) >= rowLength));
    }
}
