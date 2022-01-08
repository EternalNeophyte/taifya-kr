package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class BinaryNumTable extends LexemTable {

    public BinaryNumTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.BINARY_NUM;
    }
}
