package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.struct.lex.LexType;

public class OctetNumTable extends LexemTable {


    public OctetNumTable(String source) {
        super(source);
    }

    @Override
    public String mask() {
        return LexType.OCTET_NUM.mask();
    }
}
