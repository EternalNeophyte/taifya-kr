package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.LexType;
import edu.psuti.alexandrov.lex.LexUnit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 18.01.2022 by
 *
 * @author alexandrov
 */
public record Formation(FormationType type, List<LexUnit> units) {


    //Maybe List<Formation> nested

    public class UnitsTypeTree {

        private static final Map<Formation, UnitsTypeTree> POOL = new HashMap<>();
        private final Map<LexType, List<LexUnit>> body = units
                .stream()
                .collect(Collectors.groupingBy(LexUnit::type));

        private UnitsTypeTree() {
        }
    }

    public List<LexUnit> getUnitsByType(LexType type) {
        UnitsTypeTree tree = Optional
                .ofNullable(UnitsTypeTree.POOL.get(this))
                .orElseGet(() -> {
                    UnitsTypeTree newTree = new UnitsTypeTree();
                    UnitsTypeTree.POOL.put(this, newTree);
                    return newTree;
                });
        return tree.body.get(type);
    }


}
