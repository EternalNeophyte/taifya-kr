package edu.psuti.alexandrov.struct;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SelfParcing<T> implements Parsing<T> {

    protected List<T> content;

    protected SelfParcing() {

    }

    protected void parseSelf() {
        Matcher matcher = Pattern.compile(mask())
                                .matcher(input());
        try(Stream<MatchResult> results = matcher.results()) {
            content = results
                    .map(MatchResult::group)
                    .map(this::map)
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
