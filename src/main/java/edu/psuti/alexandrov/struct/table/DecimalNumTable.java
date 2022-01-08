package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class DecimalNumTable extends LexemTable {

    public DecimalNumTable(String source) {
        super(source);
    }

    @Override
    public String mask() {
        return LexType.DECIMAL_NUM.mask();
    }
}
