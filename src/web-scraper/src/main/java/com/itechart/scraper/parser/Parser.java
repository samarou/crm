package com.itechart.scraper.parser;

import com.itechart.scraper.model.Person;

import java.io.IOException;

public interface Parser {
    Person parse(String url) throws IOException;
}
