package edu.psuti.alexandrov.interpret;

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
        Map<String, ValueContainer<?>> variables,
        List<Formation> formations, BiBuffer<LexUnit,
        String> errBuffer) {

    //lookup next formation
    //nextTask()

}
