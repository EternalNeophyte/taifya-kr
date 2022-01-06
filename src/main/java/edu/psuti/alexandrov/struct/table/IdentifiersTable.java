package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.util.IntSequence;

public class IdentifiersTable extends LexemTable {

    public IdentifiersTable(String source) {
        super(source);
    }

    @Override
    protected void parseSelf() {
        super.parseSelf();
        IntSequence.reset();
    }

    @Override
    public String mask() {
        return IDENTIFIER;
    }
}
