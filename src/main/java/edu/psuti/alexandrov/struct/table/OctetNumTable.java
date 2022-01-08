package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class OctetNumTable extends LexemTable {


    public OctetNumTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.OCTET_NUM;
    }
}
