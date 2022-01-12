package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class AddOpTable extends LexemTable {

    public AddOpTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.ADD_OP;
    }
}
