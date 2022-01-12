package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class DelimitersTable extends ExternalFileTable {

    public DelimitersTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.DELIMITER;
    }
}
