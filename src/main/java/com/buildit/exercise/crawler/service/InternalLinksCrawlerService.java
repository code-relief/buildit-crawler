package com.buildit.exercise.crawler.service;

import com.buildit.exercise.crawler.exception.CrawlerException;
import com.buildit.exercise.crawler.model.Result;
import lombok.extern.java.Log;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log
public class InternalLinksCrawlerService implements Crawler {

    private final JsoupService jsoupService;

    public InternalLinksCrawlerService(final JsoupService jsoupService) {
        this.jsoupService = jsoupService;
    }

    @Override
    public Result crawl(final String url) {
        Result result = new Result();
        result.setUrl(url);
        Set<String> visited = new HashSet<>();
        String urlRoot = getLinkRootUrl(url);
        followLink(url, urlRoot, result, visited);
        return result;
    }

    private void followLink(String link, final String urlRoot, final Result result, final Set<String> visited) {
        if (!visited.contains(link)) {
            visited.add(link);
            if (isRelative(link)) {
                link = urlRoot + link;
            }
            Optional<Document> webPageResult = jsoupService.getWebPage(link);
            if (!webPageResult.isPresent()) {
                return;
            }
            Document doc = webPageResult.get();
            Map<Boolean, Set<String>> links = doc
                    .select("a")
                    .stream()
                    .map(a -> a.attr("href"))
                    .collect(
                            Collectors
                                    .partitioningBy(
                                            href -> isLocal(href, urlRoot),
                                            Collectors.toSet()));
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

    private boolean isLocal(String link, String rootUrl) {
        return link.matches(rootUrl.replaceAll("https?://", "^https?://") + ".*")
                || isRelative(link);
    }

    private boolean isRelative(String link) {
        return link.matches("^(\\.?/|#).+");
    }

}
