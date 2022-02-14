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


    public static record LexPosition(int line, int column) { }

    public LexPosition computePosition(LexUnit unit) {
        int line = 0, flatPos = unit.result().start();
        while (wrapPositions[line] < flatPos) {
            line++;
        }
        int column = line > 0 ? flatPos - wrapPositions[line - 1] : flatPos;
        return new LexPosition(line + 1, column);
    }

    public void run() {
        if(errors.isEmpty()) {
            try {
                formations.forEach(formation -> formation.deployIn(this));
            }
            catch (IllegalLexException e) {
                errors.put(e.unit(), e.getMessage());
            }
            catch (RuntimeException e) {
                errors.put(LexUnit.STUB, e.getMessage());
            }
        }
        errors.forEach((unit, message) -> {
            LexPosition position = computePosition(unit);
            System.out.printf("Строка %-4d | Cтолбец %-4d | %s\n", position.line(), position.column(), message);
        });
    }
}
