package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.struct.lex.LexType;

public class FloatNumTable extends LexemTable {

    public FloatNumTable(String source) {
        super(source);
    }

    @Override
    public String mask() {
        return LexType.FLOAT_NUM.mask();
    }
}
