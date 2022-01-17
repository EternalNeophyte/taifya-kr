package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Matching;
import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;

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


    public void parse(Stream<LexUnit> units) {
        final List<LexType> buffer = new LinkedList<>();
        units.peek(u -> {
            buffer.add(u.type());
             Formation.all()
                    .forEach(f -> f.expression()
                            .compute(buffer)
                            .ifComplete(m -> { }));
        });
    }
}
