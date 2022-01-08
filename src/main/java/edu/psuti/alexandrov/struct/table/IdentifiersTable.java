package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class IdentifiersTable extends LexemTable {

    public IdentifiersTable(String source) {
        super(source);
    }

    @Override
    public String mask() {
        return LexType.IDENTIFIER.mask();
    }
}
