package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.exp.Matching;
import edu.psuti.alexandrov.lex.LexType;

import java.util.List;
import java.util.stream.Stream;

import static edu.psuti.alexandrov.lex.LexType.*;

public enum Formation implements StreamingEnum<Formation, LexType, Matching>  {

    DESCRIPTION(expression()
            .one(IDENTIFIER)
            .maybeCarousel(DELIMITER, IDENTIFIER)
            .one(DELIMITER)
            .one(KEYWORD)
            .one(DELIMITER)),
    //ВЫРАЖЕНИЕ
    //ОПЕРАТОР
    MULTIPLIER(expression()
            .one(IDENTIFIER, FLOAT_NUM, BINARY_NUM, OCTET_NUM, DECIMAL_NUM, HEX_NUM)),
    CYCLE(expression().one(DELIMITER))
    ;

    Formation(Expression<LexType> expression) {
        this.expression = expression;
    }

    private final Expression<LexType> expression;

    @Override
    public Formation[] valuesFactory() {
        return values();
    }

    @Override
    public Stream<Matching> targetStream(Stream<Formation> values, List<LexType> source) {
        return values.map(v -> v.expression.compute(source)).sorted();
    }
}
