package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexUnit;
import edu.psuti.alexandrov.util.BiBuffer;

import java.util.List;
import java.util.Map;

/**
 * Created on 17.01.2022 by
 *
 * @author alexandrov
 */
public record RuntimeContext(
        Map<String, Container<?>> variables,
        List<Formation> formations,
        BiBuffer<LexUnit, String> errors,
        int[] wrapPositions) {

    //List runnables

    public static record LexPosition(int line, int column) { }

    public LexPosition computePosition(LexUnit unit) {
        int line = 0, flatPos = unit.result().start();
        while (line < wrapPositions.length && wrapPositions[line] < flatPos) {
            line++;
        }
        int column = line > 0 ? flatPos - wrapPositions[line - 1] : flatPos;
        return new LexPosition(line + 1, column);
    }

    public void tryRun() {
        if(!formations.get(formations.size() - 1)
                        .type().equals(FormationType.END)) {
            errors.put(null, "Не найден 'END' в конце программы");
        }
        if(errors.isEmpty()) {
            try {
                formations.forEach(formation -> formation.deployIn(this));
            }
            catch (IllegalLexException e) {
                errors.put(e.unit(), e.getMessage());
            }
            catch (RuntimeException e) {
                errors.put(null, e.getMessage());
            }
        }
    }
}
