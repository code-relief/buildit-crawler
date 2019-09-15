package com.buildit.exercise.crawler.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Result {

    public Result() {
        internalLinks = new HashSet<>();
        externalLinks = new HashSet<>();
        staticLinks = new HashSet<>();
    }

    private String url;
    private Set<String> internalLinks;
    private Set<String> externalLinks;
    private Set<String> staticLinks;

}
