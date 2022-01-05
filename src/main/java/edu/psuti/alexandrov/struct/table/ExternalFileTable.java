package edu.psuti.alexandrov.struct.table;

import edu.psuti.alexandrov.struct.Lexem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExternalFileTable extends LexemTable {

    public ExternalFileTable(String source) {
        super(source);
    }

    @Override
    public String mask() {
        return "s:(\n)|(\r\n)";
    }

    @Override
    public String input() {
        try {
            return Files.readString(Path.of(source));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Lexem map(String sample) {
        String[] pair = sample.split(SPACES);
        return new Lexem(Integer.parseInt(pair[0]), pair[1]);
    }
}
