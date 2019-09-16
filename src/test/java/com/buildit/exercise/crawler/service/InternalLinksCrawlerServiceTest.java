package com.buildit.exercise.crawler.service;

import com.buildit.exercise.crawler.exception.CrawlerException;
import com.buildit.exercise.crawler.model.Result;
import org.assertj.core.api.Assertions;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class InternalLinksCrawlerServiceTest {

    @Mock
    private JsoupService jsoupService;

    @InjectMocks
    private InternalLinksCrawlerService crawlerService;

    @Test
    public void shouldDealWithEmptyWebPage() {
        String webPageUrl = "http://web.page.url";
        Document doc = Mockito.mock(Document.class);
        Mockito.when(doc.select(ArgumentMatchers.eq("a,area"))).thenReturn(new Elements(Collections.emptyList()));
        Mockito.when(doc.select(ArgumentMatchers.eq("img"))).thenReturn(new Elements(Collections.emptyList()));
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.eq(webPageUrl))).thenReturn(Optional.of(doc));

        Result result = crawlerService.crawl(webPageUrl);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUrl()).isEqualTo(webPageUrl);
        Assertions.assertThat(result.getExternalLinks()).isEmpty();
        Assertions.assertThat(result.getInternalLinks()).isEmpty();
        Assertions.assertThat(result.getStaticLinks()).isEmpty();
    }

    @Test(expected = CrawlerException.class)
    public void shouldThrowCrawlerExceptionOnIncorrectUrl() {
        crawlerService.crawl("");
    }

    @Test(expected = CrawlerException.class)
    public void shouldNotInternallyCatchCrawlerException() {
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.anyString())).thenThrow(CrawlerException.class);
        crawlerService.crawl("https://some.web.page");
    }

    @Test
    public void shouldRequestOriginalWebPageUrl() {
        String webPageUrl = "http://web.page.url/my/additional/resource?item=123&limit=10";
        Document doc = Mockito.mock(Document.class);
        Mockito.when(doc.select(ArgumentMatchers.eq("a,area"))).thenReturn(new Elements(Collections.emptyList()));
        Mockito.when(doc.select(ArgumentMatchers.eq("img"))).thenReturn(new Elements(Collections.emptyList()));
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.eq(webPageUrl))).thenReturn(Optional.of(doc));

        Result result = crawlerService.crawl(webPageUrl);

        Mockito.verify(jsoupService, Mockito.times(1)).getWebPage(webPageUrl);
    }

    @Test
    public void shouldDealWhenPageResourceNotFound() {
        String webPageUrl = "http://web.page.url";
        String link1 = webPageUrl + "/href/link1";
        Document doc = Mockito.mock(Document.class);
        Element el1 = getLinkElementMock(Type.A, link1);
        Mockito.when(doc.select(ArgumentMatchers.eq("a,area"))).thenReturn(new Elements(el1));
        Mockito.when(doc.select(ArgumentMatchers.eq("img"))).thenReturn(new Elements(Collections.emptyList()));
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.eq(webPageUrl))).thenReturn(Optional.of(doc));
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.eq(link1))).thenReturn(Optional.empty());

        Result result = crawlerService.crawl(webPageUrl);

        Mockito.verify(jsoupService, Mockito.times(1)).getWebPage(webPageUrl);
        Mockito.verify(jsoupService, Mockito.times(1)).getWebPage(link1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUrl()).isEqualTo(webPageUrl);
        Assertions.assertThat(result.getExternalLinks()).isEmpty();
        Assertions.assertThat(result.getInternalLinks()).containsExactly(link1);
        Assertions.assertThat(result.getStaticLinks()).isEmpty();
    }

    @Test
    public void shouldFindExpectedNUmbersOfWebPageLInks() {
        String webPageUrl = "http://web.page.url";
        String externalWebPageUrl = "https://external.wep.page";
        String link1 = webPageUrl + "/href/link1";
        String link2 = externalWebPageUrl + "/href/link2";
        String link3 = webPageUrl + "/href/link3";
        String link4 = webPageUrl + "/src/link1";
        String link5 = externalWebPageUrl + "/src/link2";
        String link6 = externalWebPageUrl + "/src/link3";
        String relativeLink1 = "/simple/link.html";
        String relativeLink2 = "./dynamic/parametrized/link?source=abc&limit=10";
        Document doc = Mockito.mock(Document.class);
        Document innerDoc = Mockito.mock(Document.class);
        Mockito.when(innerDoc.select(ArgumentMatchers.eq("a,area"))).thenReturn(new Elements(Collections.emptyList()));
        Mockito.when(innerDoc.select(ArgumentMatchers.eq("img"))).thenReturn(new Elements(Collections.emptyList()));
        Element el1 = getLinkElementMock(Type.A, link1);
        Element el2 = getLinkElementMock(Type.A, link2);
        Element el3 = getLinkElementMock(Type.AREA, link3);
        Element el4 = getLinkElementMock(Type.IMG, link4);
        Element el5 = getLinkElementMock(Type.IMG, link5);
        Element el6 = getLinkElementMock(Type.IMG, link6);
        Element relEl1 = getLinkElementMock(Type.A, relativeLink1);
        Element relEl2 = getLinkElementMock(Type.A, relativeLink2);
        Mockito.when(doc.select(ArgumentMatchers.eq("a,area"))).thenReturn(new Elements(el1, el2, el3, relEl1, relEl2));
        Mockito.when(doc.select(ArgumentMatchers.eq("img"))).thenReturn(new Elements(el4, el5, el6));
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.eq(webPageUrl))).thenReturn(Optional.of(doc));
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.eq(link1))).thenReturn(Optional.of(innerDoc));
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.eq(link3))).thenReturn(Optional.of(innerDoc));
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.eq(webPageUrl + relativeLink1))).thenReturn(Optional.of(innerDoc));
        Mockito.when(jsoupService.getWebPage(ArgumentMatchers.eq(webPageUrl + relativeLink2))).thenReturn(Optional.of(innerDoc));

        Result result = crawlerService.crawl(webPageUrl);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUrl()).isEqualTo(webPageUrl);
        Assertions.assertThat(result.getExternalLinks()).containsExactlyInAnyOrder(link2);
        Assertions.assertThat(result.getInternalLinks())
                .containsExactlyInAnyOrder(link1, link3, relativeLink1, relativeLink2);
        Assertions.assertThat(result.getStaticLinks()).containsExactlyInAnyOrder(link4, link5, link6);

        Mockito.verify(jsoupService, Mockito.times(1)).getWebPage(webPageUrl);
        Mockito.verify(jsoupService, Mockito.times(1)).getWebPage(link1);
        Mockito.verify(jsoupService, Mockito.never()).getWebPage(link2);
        Mockito.verify(jsoupService, Mockito.times(1)).getWebPage(link3);
        Mockito.verify(jsoupService, Mockito.never()).getWebPage(link4);
        Mockito.verify(jsoupService, Mockito.never()).getWebPage(link5);
        Mockito.verify(jsoupService, Mockito.never()).getWebPage(link6);
        Mockito.verify(jsoupService, Mockito.times(1)).getWebPage(webPageUrl + relativeLink1);
        Mockito.verify(jsoupService, Mockito.times(1)).getWebPage(webPageUrl + relativeLink2);
    }

    private Element getLinkElementMock(Type type, String url) {
        Element el = Mockito.mock(Element.class);
        Mockito.when(el.attr(ArgumentMatchers.eq(type.getLinkAttr()))).thenReturn(url);
        return el;
    }

    private enum Type {
        A("href"), IMG("src"), AREA("href");
        private final String linkAttr;

        Type(final String linkAttr) {
            this.linkAttr = linkAttr;
        }

        public String getLinkAttr() {
            return linkAttr;
        }
    }

}
