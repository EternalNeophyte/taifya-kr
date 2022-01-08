package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class BinaryNumTable extends LexemTable {

    public BinaryNumTable(String source) {
        super(source);
    }

    @Override
    public String mask() {
        return LexType.BINARY_NUM.mask();
    }
}
