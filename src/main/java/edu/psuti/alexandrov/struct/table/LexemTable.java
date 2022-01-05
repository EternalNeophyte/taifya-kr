package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.struct.LexUnit;
import edu.psuti.alexandrov.struct.Lexem;
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
    public String input() {
        return source;
    }

    @Override
    public Lexem map(String sample) {
        return new Lexem(IntSequence.next(), sample);
    }

    public Optional<LexUnit> find(int tableNum, String value) {
        for(Lexem lexem : content) {
            if(lexem.value().equals(value)) {
                return Optional.of(new LexUnit(tableNum, lexem));
            }
        }
        return Optional.empty();
    }
}
