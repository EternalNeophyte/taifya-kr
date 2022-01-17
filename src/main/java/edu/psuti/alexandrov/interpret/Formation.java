package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.lex.LexType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static edu.psuti.alexandrov.lex.LexType.*;

public enum Formation {

    VAR_DEF(LexType.expression()
            .one(IDENTIFIER)
            .maybeCarousel(DELIMITER, IDENTIFIER)
            .one(DELIMITER)
            .one(TYPE_DEF)
            .one(DELIMITER)),
    //ВЫРАЖЕНИЕ
    //ОПЕРАТОР
    MULTIPLIER(LexType.expression()
            .one(IDENTIFIER, FLOAT_NUM, BINARY_NUM, OCTET_NUM, DECIMAL_NUM, HEX_NUM)),
    CYCLE(LexType.expression().one(DELIMITER))
    ;

    private static final Stream<Formation> ALL = Arrays.stream(values());
    private static final List<Formation> ALL_LIST = Arrays.asList(values());
    private final Expression<LexType> expression;
    //private final BiConsumer<List<LexUnit>, RuntimeContext> handler

    Formation(Expression<LexType> expression) {
        this.expression = expression;
    }

    public Expression<LexType> expression() {
        return expression;
    }

    public static Stream<Formation> all() {
        return ALL;
    }

    public static List<Formation> allAsList() {
        return ALL_LIST;
    }

}
