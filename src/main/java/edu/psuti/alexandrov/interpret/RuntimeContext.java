package edu.psuti.alexandrov.interpret;

import edu.psuti.alexandrov.lex.IllegalLexException;
import edu.psuti.alexandrov.lex.LexUnit;
import edu.psuti.alexandrov.ui.UIFrame;
import edu.psuti.alexandrov.util.BiBuffer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static edu.psuti.alexandrov.util.OpUtil.toRpnString;

/**
 * Created on 17.01.2022 by
 *
 * @author alexandrov
 */
public record RuntimeContext
        (Map<String, Container<?>> variables,
         List<Formation> formations,
         List<Consumer<UIFrame>> uiHandlers,
         BiBuffer<LexUnit, String> errors,
         LinkedList<ArithmeticOpPosition> opPositions,
         int[] wrapPositions)
{

    public RuntimeContext(List<Formation> formations, BiBuffer<LexUnit, String> errors, int[] wrapPositions) {
        this(new HashMap<>(), formations, new LinkedList<>(), errors, new LinkedList<>(), wrapPositions);
    }

    public static record LexPosition(int line, int column) { }

    public LexPosition computePosition(LexUnit unit) {
        int line = 0, flatPos = unit.result().start();
        while (line < wrapPositions.length && wrapPositions[line] < flatPos) {
            line++;
        }
        int column = line > 0 ? flatPos - wrapPositions[line - 1] : flatPos;
        return new LexPosition(line + 1, column + 1);
    }

    public static record ArithmeticOpPosition(int start, AtomicInteger end) { }

    public void bindArithmeticOp(int start) {
        opPositions.add(new ArithmeticOpPosition(start, new AtomicInteger(start)));
    }

    public void rearrangeArithmeticOp() {
        opPositions.getLast().end().incrementAndGet();
    }

    public boolean hasEnd() {
        return !formations.isEmpty() && formations.get(formations.size() - 1)
                                                    .type().equals(FormationType.END);
    }

    public boolean runWithNoErrors() {
        if(!hasEnd()) {
            errors.put(null, "Не найден 'end' в конце программы");
        }
        if(errors.isEmpty()) {
            try {
                formations.forEach(formation -> formation.deployIn(this));
            }
            catch (IllegalLexException e) {
                errors.put(e.unit(), e.getMessage());
            }
            catch (Throwable e) {
                errors.put(null, e.getMessage());
            }
        }
        return errors.isEmpty();
    }

    //Reverse Polish notation, RPN
    public void forEachRpn(Consumer<String> consumer) {
        opPositions.forEach(pos -> {
            String notation = toRpnString(this, formations.subList(pos.start, pos.end.get()));
            consumer.accept(notation);
        });
    }

    public Optional<Container<?>> optionalOfVar(LexUnit varDefinition) {
        String varName = varDefinition.toString();
        return Optional.ofNullable(variables.get(varName));
    }

}
