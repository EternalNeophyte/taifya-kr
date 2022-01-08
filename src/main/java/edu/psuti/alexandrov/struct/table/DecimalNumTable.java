package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class DecimalNumTable extends LexemTable {

    public DecimalNumTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.DECIMAL_NUM;
    }
}
