package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.util.IOUtil;

import java.util.stream.Stream;

public class LexAnalyzer {

    private static final String LEX_DELIMITER = "%";

    public static Stream<LexUnit> units() {
        String content = IOUtil.readTxt("samples\\program1")
                .replaceAll("\\s+", LEX_DELIMITER);
        StringBuilder sb = new StringBuilder(content);
        return LexType.all()
                .flatMap(type -> type
                        .match(sb.toString())
                        .peek(result -> {
                            int start = result.start();
                            int end = result.end();
                            sb.replace(start, end, LEX_DELIMITER.repeat(end - start));
                        })
                        .map(result -> new LexUnit(type, result))
                )
                .sorted();
    }
}
