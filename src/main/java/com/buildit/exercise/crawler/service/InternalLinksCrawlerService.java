package com.buildit.exercise.crawler.service;

import com.buildit.exercise.crawler.exception.CrawlerException;
import com.buildit.exercise.crawler.model.Result;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log
public class InternalLinksCrawlerService implements Crawler {
    @Override
    public Result crawl(final String url) {
        Result result = new Result();
        Set<String> visited = new HashSet<>();
        String urlRoot = clean(getLinkRootUrl(url));
        followLink(url, urlRoot, result, visited);
        return result;
    }

    private void followLink(final String link, final String urlRoot, final Result result, final Set<String> visited) {
        if (!visited.contains(link)) {
            visited.add(link);
            Document doc;
            try {
                doc = Jsoup.connect(link).get();
            } catch (IOException e) {
                throw new CrawlerException(e);
            }
            Map<Boolean, List<String>> links = doc
                    .select("a")
                    .stream()
                    .map(a -> clean(a.attr("href")))
                    .collect(Collectors.partitioningBy(href -> href.startsWith(urlRoot) || href.startsWith("/")));
            result.getInternalLinks().addAll(links.get(true));
            result.getExternalLinks().addAll(links.get(false));
            result.getStaticLinks().addAll(doc
                    .select("img")
                    .stream()
                    .map(a -> a.attr("src")).collect(Collectors.toSet()));
            links.get(true).forEach(l -> followLink(l, urlRoot, result, visited));
        }
    }

    private String getLinkRootUrl(String link) {
        Pattern p = Pattern.compile("https?://[^/]+");
        Matcher m = p.matcher(link);
        if (m.find()) {
            return m.group(0);
        }
        throw new CrawlerException(String.format("Cannot find root URL for: %s", link));
    }

    private String clean(final String link) {
        return link.replace("https", "http").toLowerCase().trim();
    }

}
