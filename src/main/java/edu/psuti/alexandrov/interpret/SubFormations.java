package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.util.ArraySampler;

import java.util.Arrays;

import static edu.psuti.alexandrov.lex.LexType.*;

public interface SubFormations {

    ArraySampler<LexType> LEX_TYPE_SAMPLER = ArraySampler.setup(LexType[]::new);

    LexType[]
    ANYTHING = values(),
    OPERAND = new LexType[] {
            IDENTIFIER, FLOAT_NUM, BINARY_NUM, OCTET_NUM, DECIMAL_NUM, HEX_NUM
    },
    ARITHMETIC_OP = new LexType[] {
        ADD_OP, MULTIPLY_OP
    },
    IF_CONTENT = new LexType[] {

    };


}