package com.buildit.exercise.crawler.service;

import org.jsoup.nodes.Document;

import java.util.Optional;

public interface JsoupService {

    Optional<Document> getWebPage(String url);

}
