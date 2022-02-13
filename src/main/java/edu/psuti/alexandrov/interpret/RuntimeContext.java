package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexUnit;
import edu.psuti.alexandrov.util.BiBuffer;

import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;

/**
 * Created on 17.01.2022 by
 *
 * @author alexandrov
 */
public record RuntimeContext(
        Map<String, Container<?>> variables,
        List<Formation> formations,
        BiBuffer<LexUnit, String> errors) {

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
            MatchResult result = unit.result();
            System.out.printf("Позиция %4d-%-4d: %s\n", result.start(), result.end(), message);
        });
    }
}
