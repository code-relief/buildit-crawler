package com.buildit.exercise.crawler.exception;

public class CrawlerException extends RuntimeException {

    public CrawlerException(String message) {
        super(message);
    }

    public CrawlerException(Throwable e) {
        super(e);
    }

}
