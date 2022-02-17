package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.interpret.Formation;
import edu.psuti.alexandrov.interpret.FormationType;
import edu.psuti.alexandrov.interpret.RuntimeContext;
import edu.psuti.alexandrov.util.BiBuffer;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class LexAnalyzer {

    private static final String LEX_DELIMITER = "%";
    private static final Pattern LINE_WRAPPING = Pattern.compile("\n");

    private static int[] getWrapPositions(String content) {
        return LINE_WRAPPING
                .matcher(content)
                .results()
                .mapToInt(MatchResult::start)
                .toArray();
    }

    public static RuntimeContext setupRuntimeContext(String content) {
        int[] wrapPositions = getWrapPositions(content);
        StringBuilder sb = new StringBuilder(content.replaceAll("\\s", LEX_DELIMITER));

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
                        (list, unit) -> findFormation(unit, lexBuffer, errBuffer).ifPresent(list::add),
                        LinkedList::addAll);
        if(!lexBuffer.isEmpty()) {
            formations.add(Formation.of(FormationType.INCORRECT, lexBuffer.firstHalf()));
        }
        return new RuntimeContext(new HashMap<>(), formations, errBuffer, wrapPositions);
    }

    public static Optional<Formation> findFormation(LexUnit unit,
                                                    BiBuffer<LexUnit, LexType> lexBuffer,
                                                    BiBuffer<LexUnit, String> errBuffer) {
        lexBuffer.put(unit, unit.type());
        return FormationType
                .atLeastOne(lexBuffer.secondHalf())
                .map(mi -> switch (mi.matching().type()) {
                                case COMPLETE -> {
                                    List<LexUnit> units = lexBuffer.copyFirstHalf();
                                    lexBuffer.clear();
                                    errBuffer.clear();
                                    yield Formation.of(mi.item(), units);
                                }
                                case PARTIAL -> null;
                                case NO -> {
                                    errBuffer.put(unit, unit.type() + " не ожидается здесь");
                                    yield null;
                                }
                            }
                );
    }
}
