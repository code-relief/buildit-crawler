package com.buildit.exercise.crawler.service;

import com.buildit.exercise.crawler.model.Result;

public interface Crawler {

    Result crawl(String url);

}
