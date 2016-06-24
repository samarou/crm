package com.itechart.scraper.parser;

import com.itechart.scraper.model.SmgProfile;

import java.io.IOException;

public interface Parser {
    SmgProfile parse(String url) throws IOException;
}
