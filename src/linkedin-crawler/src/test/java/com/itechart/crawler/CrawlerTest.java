package com.itechart.crawler;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by anton.charnou on 30.05.2016.
 */
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
