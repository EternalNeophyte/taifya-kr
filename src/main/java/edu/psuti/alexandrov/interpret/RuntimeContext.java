package edu.psuti.alexandrov.interpret;

import java.util.List;
import java.util.Map;

/**
 * Created on 17.01.2022 by
 *
 * @author alexandrov
 */
public record RuntimeContext(
        Map<String, ValueContainer<?>> variables,
        List<Formation> formations) {

    //lookup next formation
    //nextTask()

}
