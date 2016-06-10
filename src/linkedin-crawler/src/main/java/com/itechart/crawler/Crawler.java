package com.itechart.crawler;


import java.io.IOException;

/**
 * Created by anton.charnou on 30.05.2016.
 */
public interface Crawler {
    void crawl(String fileName) throws IOException;
}
