package com.itechart.scraper;

import com.itechart.scraper.parser.Parser;
import com.itechart.scraper.parser.impl.SmgProfileParser;
import org.junit.Test;

import java.io.IOException;

public class SmgServiceTest {
    @Test
    public void parseSmgTest() throws IOException {
        Parser smgService = new SmgProfileParser();
        System.out.println(smgService.parse("828"));
    }
}
