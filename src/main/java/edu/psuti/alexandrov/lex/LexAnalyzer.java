package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.struct.SelfParcing;
import edu.psuti.alexandrov.struct.table.*;
import edu.psuti.alexandrov.util.IOUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.psuti.alexandrov.lex.LexType.*;

public class LexAnalyzer extends SelfParcing<String> {

    private final Map<LexType, LexemTable> tableMap;

    public LexAnalyzer() {
        String input = input();
        tableMap = Stream.of(
                    new KeywordsTable("tables\\keywords"),
                    new TypeDefTable(input),
                    new DelimitersTable("tables\\delimiters"),
                    new CompareOpTable(input),
                    new AddOpTable(input),
                    new MultiplyOpTable(input),
                    new IdentifiersTable(input),
                    new BinaryNumTable(input),
                    new OctetNumTable(input),
                    new HexNumTable(input),
                    new DecimalNumTable(input),
                    new FloatNumTable(input)
                )
                .collect(Collectors.toUnmodifiableMap(
                    LexemTable::lexType,
                    Function.identity())
                );
    }

    @Override
    public String mask() {
        try(Stream<Lexem> lexems = tableMap.get(DELIMITER).content()) {
            return WORD_CHARS + OR + lexems
                    .map(lex -> {
                        String value = lex.value();
                        return value.length() == 1 ? SCREEN + value : value;
                    })
                    .collect(Collectors.joining(OR));
        }
    }

    @Override
    public String input() {
        return IOUtil.readTxt("samples\\program1")
                     .replaceAll(SPACES, LEX_DELIMITER);
    }

    @Override
    public String map(String sample) {
        return sample;
    }

    public Stream<LexUnit> lexUnits() {
        prepareContent();
        return content().map(this::findInTables);
    }

    private void prepareContent() {
        if(Objects.isNull(content)) {
            parse();
            Iterator<String> it = content.iterator();
            boolean commentDetected = false;
            while(it.hasNext()) {
                String next = it.next();
                if(next.equals(OPEN_COMMENT)) {
                    commentDetected = true;
                }
                if(commentDetected || next.equals(EMPTY)) {
                    it.remove();
                }
                if(next.equals(CLOSE_COMMENT)) {
                    commentDetected = false;
                }
            }
        }
    }

    private LexUnit findInTables(String lex) {
        for(LexemTable table : tableMap.values()) {
            LexUnit lexUnit = table.find(lex);
            if(Objects.nonNull(lexUnit)) {
                return lexUnit;
            }
        }
        return LexUnit.unknown(lex);
    }

}
