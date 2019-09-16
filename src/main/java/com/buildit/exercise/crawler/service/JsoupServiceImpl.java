package com.buildit.exercise.crawler.service;

import com.buildit.exercise.crawler.exception.CrawlerException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupServiceImpl implements JsoupService {

    @Override
    public Document getWebPage(final String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new CrawlerException(e);
        }
    }
}
