package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;

import java.util.*;
import java.util.stream.Collectors;

import static edu.psuti.alexandrov.lex.LexType.notEquals;

/**
 * Created on 18.01.2022 by
 *
 * @author alexandrov
 */
public record Formation(FormationType type, List<Formation> nested, List<LexUnit> units) {


    //Maybe List<Formation> nested

    public class UnitsTypeTree {

        private static final Map<Formation, UnitsTypeTree> POOL = new HashMap<>();
        private final Map<LexType, List<LexUnit>> body = units
                .stream()
                .collect(Collectors.groupingBy(LexUnit::type));

        private UnitsTypeTree() {
        }
    }

    public List<LexUnit> unitsListOfType(LexType type) {
        UnitsTypeTree tree = UnitsTypeTree.POOL.get(this);
        return Optional.ofNullable(tree)
                .or(() -> {
                    UnitsTypeTree newTree = new UnitsTypeTree();
                    UnitsTypeTree.POOL.put(this, newTree);
                    return Optional.of(newTree);
                })
                .map(t -> t.body.get(type))
                .orElse(Collections.emptyList());
    }

    public LexUnit firstUnitOfType(LexType type) {
        return Optional.of(unitsListOfType(type))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .orElseThrow(() -> new RuntimeException("Не найдено ни одной лекс. единицы типа " + type));
    }

    public List<LexUnit> unitsListInRange(LexType start, LexType end) {
        LexUnit first = firstUnitOfType(start);
        LexUnit last = firstUnitOfType(end);
        return units.subList(units.indexOf(first), units.indexOf(last));
    }

    public boolean unitsHaveTypes(int start, int end, LexType... types) {
        List<LexUnit> range = units.subList(start, end);
        int rangeSize = units.size();
        if(rangeSize != types.length) {
            return false;
        }
        for(int i = 0; i < rangeSize; i++) {
            if(notEquals(range.get(i).type(), types[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean unitsHaveTypes(LexUnit start, LexUnit end, LexType... types) {
        return unitsHaveTypes(units.indexOf(start), units.indexOf(end), types);
    }

    public void deployIn(RuntimeContext context) {
        if(Objects.nonNull(nested)) {
            nested.forEach(formation -> formation.deployIn(context));
        }
        type.action().accept(this, context);
    }
}
