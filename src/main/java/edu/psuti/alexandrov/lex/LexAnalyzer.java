package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.interpret.Formation;
import edu.psuti.alexandrov.interpret.RuntimeContext;
import edu.psuti.alexandrov.util.BiBuffer;
import edu.psuti.alexandrov.util.IOUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;

import static edu.psuti.alexandrov.interpret.FormationType.atLeastOne;

public class LexAnalyzer {

    private static final String LEX_DELIMITER = "%";

    public static RuntimeContext toRuntimeContext() {
        String content = IOUtil.readTxt("samples\\program1")
                .replaceAll("\\s+", LEX_DELIMITER);
        StringBuilder sb = new StringBuilder(content);
        BiBuffer<LexUnit, LexType> lexBuffer = BiBuffer.basedOnLinkedList();
        BiBuffer<LexUnit, String> errBuffer = BiBuffer.basedOnLinkedList();
        List<Formation> formations = LexType.all()
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
                            lexBuffer.put(unit, unit.type());
                            atLeastOne(lexBuffer.secondHalf(),
                                    mi -> {
                                        switch (mi.matching().type()) {
                                            case COMPLETE -> {
                                                list.add(new Formation(mi.item(), lexBuffer.copyFirstHalf()));
                                                lexBuffer.clear();
                                                errBuffer.clear();
                                            }
                                            case NO -> errBuffer.put(unit, "Неожиданный " +  unit.type());
                                        }
                                    });
                        },
                        LinkedList::addAll);
        return new RuntimeContext(new HashMap<>(), formations, errBuffer);
    }
}
