package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;

import java.util.*;
import java.util.stream.Collectors;

import static edu.psuti.alexandrov.lex.LexType.notEquals;


public record Formation(FormationType type, List<Formation> nested, List<LexUnit> units) {

    public static Formation of(FormationType type, List<LexUnit> units) {
        return new Formation(type, new LinkedList<>(), units);
    }

    public class UnitsTypeTree {

        private static final Map<Formation, UnitsTypeTree> POOL = new HashMap<>();
        private final Map<LexType, List<LexUnit>> body = units
                .stream()
                .collect(Collectors.groupingBy(LexUnit::type));

        private UnitsTypeTree() { }
    }

    public LexUnit firstUnitOfTypeOrThrow(LexType... types) {
        return orderedUnitOfType(0, types)
                .orElseThrow(() -> new IllegalArgumentException("Ошибка интерпретатора:" +
                                            " не найдено ни одной лекс. единицы среди типов" +
                                            Arrays.toString(types)));
    }

    public Optional<LexUnit> firstUnitOfType(LexType... types) {
        return orderedUnitOfType(0, types);
    }

    public LexUnit orderedUnitOfTypeOrThrow(int order, LexType... types) {
        return orderedUnitOfType(0, types)
                .orElseThrow(() -> new IllegalArgumentException("Ошибка интерпретатора:" +
                                            " вхождение лекс. единицы [" + order +
                                            "] не найдено среди типов" + Arrays.toString(types)));
    }

    public Optional<LexUnit> orderedUnitOfType(int order, LexType... types) {
        return Optional.of(unitsListOfType(types))
                .map(list -> {
                    try {
                        return list.get(order);
                    }
                    catch (IndexOutOfBoundsException e) {
                        return null;
                    }
                });
    }

    public List<LexUnit> unitsListOfType(LexType... types) {
        UnitsTypeTree tree = UnitsTypeTree.POOL.get(this);
        return Optional.ofNullable(tree)
                .or(() -> {
                    UnitsTypeTree newTree = new UnitsTypeTree();
                    UnitsTypeTree.POOL.put(this, newTree);
                    return Optional.of(newTree);
                })
                .map(t -> Arrays.stream(types)
                        .map(t.body::get)
                        .filter(Objects::nonNull)
                        .flatMap(List::stream)
                        .toList())
                .orElseGet(Collections::emptyList);
    }

    public List<LexUnit> unitsListInRange(LexType start, LexType end) {
        return orderedUnitOfType(0, start)
                .flatMap(first -> orderedUnitOfType(0, end)
                                    .map(last -> {
                                            try {
                                                return units.subList(units.indexOf(first),
                                                                    units.indexOf(last));
                                            }
                                            catch (IndexOutOfBoundsException e) {
                                                return null;
                                            }
                                    }))
                .orElseGet(Collections::emptyList);
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
        nested.forEach(formation -> formation.deployIn(context));
        type.action().accept(this, context);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n").append(type);
        for(int i = 0; i < units.size(); i++) {
            LexUnit unit = units.get(i);
            sb.append(String.format("\n%3d. %s [%s]", i + 1, unit.type(), unit));
        }
        if(!nested.isEmpty()) {
            sb.append("Вложенные конструкции (").append(type)
                                                .append(") {\n");
            nested.forEach(sb::append);
            sb.append("\n}");
        }
        return sb.toString();
    }
}
