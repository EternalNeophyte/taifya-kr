package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;
import edu.psuti.alexandrov.lex.Lexem;
import edu.psuti.alexandrov.struct.SelfParcing;
import edu.psuti.alexandrov.util.IntSequence;

import java.util.Optional;

public abstract class LexemTable extends SelfParcing<Lexem> {

    final String source;

    LexemTable(String source) {
        this.source = source;
        parseSelf();
    }

    @Override
    public String mask() {
        return lexType().mask();
    }

    @Override
    public String input() {
        return source;
    }

    @Override
    public Lexem map(String sample) {
        return new Lexem(sample);
    }

    @Override
    protected void parseSelf() {
        super.parseSelf();
        IntSequence.reset();
    }

    public LexType lexType() {
        return LexType.UNKNOWN;
    }

    public Optional<LexUnit> find(String value) {
        for(Lexem lexem : content) {
            if(lexem.value().equals(value)) {
                return Optional.of(new LexUnit(lexType(), lexem));
            }
        }
        return Optional.empty();
    }
}
