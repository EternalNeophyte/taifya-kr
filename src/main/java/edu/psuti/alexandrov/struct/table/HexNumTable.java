package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.struct.lex.LexType;

public class HexNumTable extends LexemTable {

    public HexNumTable(String source) {
        super(source);
    }

    @Override
    public String mask() {
        return LexType.HEX_NUM.mask();
    }
}
