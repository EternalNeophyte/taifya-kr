import edu.psuti.alexandrov.exp.Expression;
import edu.psuti.alexandrov.exp.pattern.WalkPattern;
import edu.psuti.alexandrov.interpret.FormationType;
import edu.psuti.alexandrov.lex.LexType;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class ParcingTest {

    @Test
    public void testProcedureSplit() {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        String sample = "fegre yjrt jyuj procedure erhe yetje procedure egy";
        Pattern.compile("((?<!procedure).)*").matcher(sample)
                .results()
                .map(MatchResult::group)
                .forEach(System.out::println);
        String[] arr = sample.split("");
        assertEquals(3, sample.split("procedure").length);

    }


    @Test
    public void testFloatAlternation() {
        Pattern pattern = Pattern.compile("^[\\d]*[.][\\d]+$|^[\\da-fA-F]+[Hh]$");
        String sample1 = "4.7558578";
        String sample2 = ".7558578";
        String sample3 = "a.563f3";
        String sample4 = "AFFD24h";
        String sample5 = "547.5869";
        assertTrue(pattern.matcher(sample1).find());
        assertTrue(pattern.matcher(sample2).find());
        assertFalse(pattern.matcher(sample3).find());
        assertTrue(pattern.matcher(sample4).find());
        assertTrue(pattern.matcher(sample5).find());
    }

    @Test
    public void testAnalyzer() {

    }

    @Test
    public void testRealLine() {
        String line = "counte8r in\ncounter 0\n";
        Pattern.compile("oun")
                .matcher(line)
                .results()
                .map(MatchResult::group)
                .forEach(System.out::println);
    }

    @Test
    public void testAlternation() {
        String line = "0.5436e9";
        Matcher m = Pattern.compile("^[\\d]*[.][\\d]+([eE][+-]?[\\d])?|[\\d]+[eE][+-]?[\\d]$").matcher(line);
        assertTrue(m.find());
    }

    @Test
    public void testRepeatedPattern() {
        Integer[] row = {1, 2, 3};
        WalkPattern<Integer> p = WalkPattern.repeatable(Expression.in(row), row);
        var m = Expression.<Integer>start()
                .maybeOne(7)
                .maybeOne(9)
                .one(7)
                .carousel(1, 2, 5)
                .maybeOne(7)
                .maybeOne(8)
                .maybeOne(5)
                .compute(7, 9, 1, 2, 5);
        m.toString();
    }

    @Test
    public void testExpMatching() {
        var m = Expression.<Integer>start()
                .one(8)
                .one(7)
                .maybeMany(3, 5)
                .one(9)
                .compute(8, 7, 3, 3, 5, 3, 9);
        m.toString();
    }

    @Test
    public void testIf() {
        var m = FormationType.IF_THEN_ELSE
                .expression()
                .compute(LexType.IF_DEF,
                        LexType.IDENTIFIER,
                        LexType.COMPARE_OP,
                        //LexType.OCTET_NUM,
                        LexType.THEN_SECTION,
                        LexType.OUTPUT_DEF,
                        LexType.START_ARGS,
                        //LexType.IDENTIFIER,
                        LexType.END_ARGS,
                        LexType.END_IF);
        m.toString();
    }

    @Test
    public void testParseNumbers() {
        double f = Double.parseDouble(".4535");
        double d = Double.parseDouble("0.245e-17");
        toString();

    }

    @Test
    public void testArithm() {
        var m = FormationType.ARITHMETIC_END_ARGS.expression()
                .compute(LexType.ADD_OP, LexType.OCTET_NUM);
        m.toString();
    }

    @Test
    public void testSplit() {
        var s = Integer.toBinaryString(7);
        s.toString();
    }


}
