package com.itechart.scraper;

import com.itechart.scraper.impl.BingSearchApiCrawler;
import com.itechart.scraper.impl.GooglePageCrawler;
import org.junit.Test;

import java.io.IOException;

public class CrawlerTest {

    @Test
    public void testGoogle() throws IOException {
        Crawler crawler = new GooglePageCrawler();
        crawler.crawl("google_test.txt");
    }

    @Test
    public void testBing() throws IOException {
        Crawler crawler = new BingSearchApiCrawler();
        crawler.crawl("bing_test.txt");
    }
}
