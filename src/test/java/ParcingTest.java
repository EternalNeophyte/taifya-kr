import edu.psuti.alexandrov.lexem.LexicAnalyzer;
import edu.psuti.alexandrov.struct.table.ExternalFileTable;
import edu.psuti.alexandrov.util.IOUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    public void testLexemTable() {
        var s = IOUtil.readTxt("tables", "keywords");
        ExternalFileTable table = new ExternalFileTable("tables\\keywords");
        table.toString();
    }

    @Test
    public void testFloatAlternation() {
        //ToDo решить проблему с парсингом чисел
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
        LexicAnalyzer analyzer = new LexicAnalyzer();
        var s = analyzer.lexUnits();
        s.forEach(System.out::println);
    }

    @Test
    public void testRealLine() {
        String line = "counte8r: int; counter == 0;".replaceAll("\\s", "");
        Pattern.compile("\\w+|[\\W]{1,2}").matcher(line).results().map(MatchResult::group).forEach(System.out::println);
    }

    @Test
    public void testAlternation() {
        String line = "kytferrum rkbmo ";
        Matcher m = Pattern.compile("rest|ferrum").matcher(line);
        assertTrue(m.find());
    }

}