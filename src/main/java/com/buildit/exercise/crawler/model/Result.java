package com.buildit.exercise.crawler.model;

import lombok.Data;

import java.util.Set;

@Data
public class Result {

    private String url;
    private Set<String> links;

}
