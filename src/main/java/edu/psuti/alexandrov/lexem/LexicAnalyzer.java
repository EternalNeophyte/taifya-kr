package edu.psuti.alexandrov.lexem;

import edu.psuti.alexandrov.struct.LexUnit;
import edu.psuti.alexandrov.struct.SelfParcing;
import edu.psuti.alexandrov.struct.table.ExternalFileTable;
import edu.psuti.alexandrov.struct.table.IdentifiersTable;
import edu.psuti.alexandrov.struct.table.NumbersTable;
import edu.psuti.alexandrov.util.IOUtil;

import java.util.Objects;
import java.util.stream.Stream;

public class LexicAnalyzer extends SelfParcing<String> {

    private final ExternalFileTable keywords;
    private final ExternalFileTable delimiters;
    private final IdentifiersTable identifiers;
    private final NumbersTable numbers;

    public LexicAnalyzer() {
        keywords = new ExternalFileTable("tables//keywords");
        delimiters = new ExternalFileTable("tables//delimiters");
        identifiers = new IdentifiersTable(input());
        numbers = new NumbersTable(input());
    }

    @Override
    public String mask() {
        return DIRTY_LEX_SPLIT;
    }

    @Override
    public String input() {
        return IOUtil.readTxt("samples\\program1")
                     .replaceAll("\\s", LEX_DELIMITER);
    }

    @Override
    public String map(String sample) {
        return sample;
    }

    public Stream<LexUnit> lexUnits() {
        prepareContent();
        return content()
                .filter(lex -> !lex.equals(EMPTY))
                .map(lex -> keywords.find(1, lex)
                        .or(() -> delimiters.find(2, lex))
                        .or(() -> identifiers.find(3, lex))
                        .or(() -> numbers.find(4, lex))
                        .orElse(LexUnit.UNKNOWN)
                );
    }

    private void prepareContent() {
        if(Objects.isNull(content)) {
            parseSelf();
        }
    }
}
