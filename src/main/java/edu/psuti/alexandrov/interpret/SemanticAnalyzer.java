package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Matching;
import edu.psuti.alexandrov.lex.LexAnalyzer;
import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created on 17.01.2022 by
 *
 * @author alexandrov
 */
public class SemanticAnalyzer {

    RuntimeContext context;

    public void parse() {
        final List<LexType> buffer = new LinkedList<>();
        LexAnalyzer.units()  //Maybe reduce
                .peek(u -> {
            buffer.add(u.type());
            Formation.all()
                    .forEach(f -> f.getExpression()
                                .compute(buffer)
                                .ifComplete(m -> {
                                    //??? List.copyOf(buffer);

                                    buffer.clear();
                                })
                    );
        });
        //Map to key - Formation and value - copy of buffer
    }
}
