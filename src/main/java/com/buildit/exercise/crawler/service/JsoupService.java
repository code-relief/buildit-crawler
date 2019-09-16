package com.buildit.exercise.crawler.service;

import org.jsoup.nodes.Document;

public interface JsoupService {

    Document getWebPage(String url);

}
