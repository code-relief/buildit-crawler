package com.buildit.exercise.crawler.service;

import com.buildit.exercise.crawler.exception.CrawlerException;
import lombok.extern.java.Log;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Log
public class JsoupServiceImpl implements JsoupService {

    @Override
    public Optional<Document> getWebPage(final String url) {
        try {
            return Optional.of(Jsoup.connect(url).get());
        } catch (HttpStatusException e) {
            log.warning(String.format("URL: '' not found", url));
            return Optional.empty();
        } catch (IOException e) {
            throw new CrawlerException(e);
        }
    }
}
