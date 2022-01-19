package edu.psuti.alexandrov.lex;

import java.util.LinkedList;
import java.util.List;

/**
 * Created on 19.01.2022 by
 *
 * @author alexandrov
 */
public record LexBuffer(List<LexType> types, List<LexUnit> units) {

    public static LexBuffer allocate() {
        return new LexBuffer(new LinkedList<>(), new LinkedList<>());
    }

    public void put(LexUnit unit) {
        types.add(unit.type());
        units.add(unit);
    }

    public void clear() {
        types.clear();
        units.clear();
    }

    public List<LexType> copyTypes() {
        return List.copyOf(types);
    }

    public List<LexUnit> copyUnits() {
        return List.copyOf(units);
    }
}
