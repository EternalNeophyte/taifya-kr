package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.Lexem;
import edu.psuti.alexandrov.util.IOUtil;

public abstract class ExternalFileTable extends LexemTable {

    public ExternalFileTable(String source) {
        super(source);
    }

    @Override
    public String mask() {
        return WORD_CHARS + SPACES + ".+" + LINE_WRAP;
    }

    @Override
    public String input() {
        return IOUtil.readTxt(source);
    }

    @Override
    public Lexem map(String sample) {
        String[] pair = sample.split(SPACES);
        return new Lexem(Integer.parseInt(pair[0]), pair[1]);
    }
}
