package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.struct.lex.LexType;
import edu.psuti.alexandrov.struct.lex.LexUnit;
import edu.psuti.alexandrov.struct.lex.Lexem;
import edu.psuti.alexandrov.parse.SelfParcing;
import edu.psuti.alexandrov.util.IntSequence;

import java.util.Optional;

public abstract class LexemTable extends SelfParcing<Lexem> {

    final String source;

    LexemTable(String source) {
        this.source = source;
        parseSelf();
    }

    @Override
    protected void parseSelf() {
        super.parseSelf();
        IntSequence.reset();
    }

    @Override
    public String input() {
        return source;
    }

    @Override
    public Lexem map(String sample) {
        return new Lexem(IntSequence.next(), sample);
    }

    public Optional<LexUnit> find(LexType type, String value) {
        for(Lexem lexem : content) {
            if(lexem.value().equals(value)) {
                return Optional.of(new LexUnit(type, lexem));
            }
        }
        return Optional.empty();
    }
}
