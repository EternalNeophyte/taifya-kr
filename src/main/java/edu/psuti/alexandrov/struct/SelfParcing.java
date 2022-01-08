package edu.psuti.alexandrov.struct;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SelfParcing<T> implements Parsing<T> {

    protected List<T> content;

    protected SelfParcing() {

    }

    protected void parseSelf() {
        String mask = mask();
        String input = input();
        Stream<String> samples = mask.startsWith(SPLIT_SIGN)
                ? Arrays.stream(input.split(mask.substring(SPLIT_SIGN.length())))
                : Pattern.compile(mask)
                        .matcher(input)
                        .results()
                        .map(MatchResult::group);
        try(samples) {
            content = samples.map(this::map)
                             .collect(Collectors.toCollection(listFactory()));
        }
    }

    public Stream<T> content() {
        return content.stream();
    }

    public T get(int index) {
        return content.get(index);
    }

}
