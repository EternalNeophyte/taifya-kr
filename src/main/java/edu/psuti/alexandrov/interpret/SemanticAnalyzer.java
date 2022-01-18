package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.LexAnalyzer;
import edu.psuti.alexandrov.lex.LexType;

import java.util.LinkedList;
import java.util.List;

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
            /*FormationType.findCompleteMatching(buffer)
                    .ifPresent(f -> f);*/
                    /*.forEach(f -> f.getExpression()
                                .compute(buffer)
                                .ifComplete(m -> {
                                    //??? List.copyOf(buffer);

                                    buffer.clear();
                                })
                    );*/
        });
        //Map to key - Formation and value - copy of buffer
    }
}
