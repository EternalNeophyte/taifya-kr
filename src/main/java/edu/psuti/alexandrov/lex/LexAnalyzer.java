package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.interpret.Formation;
import edu.psuti.alexandrov.interpret.FormationType;
import edu.psuti.alexandrov.util.IOUtil;

import java.util.LinkedList;
import java.util.List;

public class LexAnalyzer {

    private static final String LEX_DELIMITER = "%";

    public static List<Formation> formations() {
        final String content = IOUtil.readTxt("samples\\program1")
                .replaceAll("\\s+", LEX_DELIMITER);
        final StringBuilder sb = new StringBuilder(content);
        final LexBuffer buffer = LexBuffer.allocate();
        return LexType
                .all()
                .flatMap(type -> type
                        .match(sb.toString())
                        .peek(result -> {
                            int start = result.start();
                            int end = result.end();
                            sb.replace(start, end, LEX_DELIMITER.repeat(end - start));
                        })
                        .map(result -> new LexUnit(type, result))
                )
                .sorted()
                .collect(LinkedList::new,
                        (list, unit) -> {
                            buffer.put(unit);
                            //ToDo очищать буффер на каждой итерации, если во всех случаях Matching = NO
                            FormationType.findCompleteMatching(buffer.types())
                                    .ifPresent(type -> {
                                        List<LexUnit> accumulated = buffer.copyUnits();
                                        list.add(new Formation(type, accumulated));
                                        buffer.clear();
                                    });
                        },
                        LinkedList::addAll);
    }
}
