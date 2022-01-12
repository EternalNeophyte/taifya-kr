package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class IdentifiersTable extends LexemTable {

    public IdentifiersTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.IDENTIFIER;
    }
}
