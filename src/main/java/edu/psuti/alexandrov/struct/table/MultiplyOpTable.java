package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class MultiplyOpTable extends LexemTable {

    public MultiplyOpTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.MULTIPLY_OP;
    }
}
