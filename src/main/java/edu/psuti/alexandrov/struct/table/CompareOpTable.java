package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class CompareOpTable extends LexemTable {

    public CompareOpTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.COMPARE_OP;
    }
}
