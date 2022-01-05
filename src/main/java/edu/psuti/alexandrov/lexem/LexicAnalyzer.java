package edu.psuti.alexandrov.lexem;

import edu.psuti.alexandrov.struct.LexUnit;
import edu.psuti.alexandrov.struct.Lexem;
import edu.psuti.alexandrov.struct.SelfParcing;
import edu.psuti.alexandrov.struct.table.ExternalFileTable;
import edu.psuti.alexandrov.struct.table.IdentifiersTable;
import edu.psuti.alexandrov.struct.table.NumbersTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LexicAnalyzer extends SelfParcing<String> {

    private final ExternalFileTable keywords;
    private final ExternalFileTable delimiters;
    private final IdentifiersTable identifiers;
    private final NumbersTable numbers;

    public LexicAnalyzer() {
        keywords = new ExternalFileTable("src/main/resources/tables/keywords.txt");
        delimiters = new ExternalFileTable("src/main/resources/tables/delimiters.txt");
        identifiers = new IdentifiersTable(input());
        numbers = new NumbersTable(input());
    }

    @Override
    public String mask() {
        return "\\w+|[\\W]{0,2}";
    }

    @Override
    public String input() {
        try {
            return Files.readString(Path.of("src/main/resources/samples/program1.txt"))
                        .replaceAll("\\s", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String map(String sample) {
        return sample;
    }

    public Stream<LexUnit> lexUnits() {
        if(Objects.isNull(content)) {
            parseSelf();
        }
        return content()
                .map(word -> keywords.find(1, word)
                        .or(() -> delimiters.find(2, word))
                        .or(() -> identifiers.find(3, word))
                        .or(() -> numbers.find(4, word))
                        .orElse(LexUnit.UNKNOWN)
                );
    }
}
