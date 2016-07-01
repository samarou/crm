package com.itechart.scraper;


import java.io.IOException;

public interface Crawler {
    void crawl(String fileName) throws IOException;
}
