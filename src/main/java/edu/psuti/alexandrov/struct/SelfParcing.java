package edu.psuti.alexandrov.struct;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SelfParcing<T> implements Parsing<T> {

    //ToDo хранить MatchResult'ы здесь, чтобы потом собирать лексемы в стрим с сортировкой по полю first
    protected List<T> content;

    protected SelfParcing() {

    }

    protected void parse() {
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

}
