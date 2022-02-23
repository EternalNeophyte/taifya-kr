package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.util.ArraySampler;

import java.util.Collections;
import java.util.Set;

import static edu.psuti.alexandrov.lex.LexType.*;

public interface SubFormations {

    Set<FormationType> NO_CONSTRAINTS = Collections.emptySet();

    ArraySampler<LexType>
    LEX_TYPE_SAMPLER = ArraySampler.setup(LexType[]::new);

    LexType[]
    ANYTHING = values(),

    ARGS_DEF = new LexType[] {
            START_ARGS, END_ARGS
    },

    OPERAND = new LexType[] {
            IDENTIFIER, FLOAT_NUM, BINARY_NUM, OCTET_NUM, DECIMAL_NUM, HEX_NUM, LOGIC_CONST
    },

    ARITHMETIC_OP = new LexType[] {
            ADD_OP, MULTIPLY_OP
    };

}
