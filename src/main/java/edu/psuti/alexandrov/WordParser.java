package edu.psuti.alexandrov;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class WordParser {

    public static void main(String... args) {
        String sample = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                 sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                 Ut enim ad minim veniam, quis nostrud exercitation\040
                 ullamco laboris nisi ut aliquip ex ea commodo consequat.\040
                 Duis aute irure dolor in reprehenderit in voluptate velit\040
                 esse cillum dolore eu fugiat nulla pariatur. Excepteur sint\040
                 occaecat cupidatat non proident, sunt in culpa qui officia\040
                 deserunt mollit anim id est laborum.
                """;
        String[] words = sample.split("\\W+");
        Arrays.stream(words)
                .map(String::toLowerCase)
                .collect(groupingBy(identity(),
                                    () -> new TreeMap<>(String::compareTo),
                                    counting()))
                .forEach((word, count) -> System.out.printf("%-15s - x%-4d\n", word, count));
    }
}
