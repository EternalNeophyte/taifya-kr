package edu.psuti.alexandrov.lex;

import edu.psuti.alexandrov.interpret.Formation;
import edu.psuti.alexandrov.interpret.FormationType;
import edu.psuti.alexandrov.interpret.RuntimeContext;
import edu.psuti.alexandrov.util.BiBuffer;
import edu.psuti.alexandrov.util.IOUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
                        (list, unit) -> findFormation(unit, lexBuffer, errBuffer).ifPresent(list::add),
                        LinkedList::addAll);
        return new RuntimeContext(new HashMap<>(), formations, errBuffer);
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
                            yield new Formation(mi.item(), null, units);
                        }
                        case PARTIAL -> null;
                        case NO -> {
                            errBuffer.put(unit, "Неожиданный " +  unit.type());
                            yield null;
                        }
                    });
    }
}
