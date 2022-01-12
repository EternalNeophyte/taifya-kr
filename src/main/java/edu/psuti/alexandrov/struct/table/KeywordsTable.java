package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;

public class KeywordsTable extends ExternalFileTable {

    public KeywordsTable(String source) {
        super(source);
    }

    @Override
    public LexType lexType() {
        return LexType.KEYWORD;
    }
}
