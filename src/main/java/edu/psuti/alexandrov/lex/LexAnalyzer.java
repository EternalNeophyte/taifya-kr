package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.exp.MatchingItem;
import edu.psuti.alexandrov.interpret.Formation;
import edu.psuti.alexandrov.interpret.FormationType;
import edu.psuti.alexandrov.interpret.RuntimeContext;
import edu.psuti.alexandrov.util.BiBuffer;
import edu.psuti.alexandrov.util.IOUtil;

import java.util.LinkedList;
import java.util.List;

public class LexAnalyzer {

    private static final String LEX_DELIMITER = "%";

    public static List<Formation> formations() {
        final String content = IOUtil.readTxt("samples\\program1")
                .replaceAll("\\s+", LEX_DELIMITER);
        final StringBuilder sb = new StringBuilder(content);
        final BiBuffer<LexUnit, LexType> lexBuffer = BiBuffer.BasedOnLinkedList.allocate();
        final BiBuffer<LexUnit, String> errBuffer = BiBuffer.BasedOnLinkedList.allocate();
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
                            lexBuffer.put(unit, unit.type());
                            MatchingItem<FormationType> first = FormationType.findFirst(lexBuffer.secondHalf());
                            switch (first.matching().type()) {
                                case COMPLETE -> {
                                    list.add(new Formation(first.item(), lexBuffer.copyFirstHalf()));
                                    lexBuffer.clear();
                                }
                                case NO -> errBuffer.put(unit, unit.type() + " not allowed here");
                            }
                        },
                        LinkedList::addAll);
    }
}
