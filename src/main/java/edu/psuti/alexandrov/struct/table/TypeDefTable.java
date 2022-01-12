package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class TypeDefTable extends LexemTable {

    public TypeDefTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.TYPE_DEF;
    }
}
