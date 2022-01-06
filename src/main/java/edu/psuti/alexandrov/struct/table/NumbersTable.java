package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.util.IntSequence;

public class NumbersTable extends LexemTable {

    public NumbersTable(String source) {
        super(source);
    }

    @Override
    protected void parseSelf() {
        super.parseSelf();
        IntSequence.reset();
    }

    @Override
    public String mask() {
        return String.join(OR, FLOAT, BINARY, OCTET, DECIMAL, HEX);
    }
}
