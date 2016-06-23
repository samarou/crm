package com.itechart.scraper;

import com.itechart.scraper.parser.Parser;
import com.itechart.scraper.parser.impl.SmgPersonParser;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by anton.charnou on 07.06.2016.
 */
public class SmgServiceTest {
    @Test
    public void parseSmgTest() throws IOException {
        Parser smgService = new SmgPersonParser();
        System.out.println(smgService.parse("https://smg.itechart-group.com/iTechArt.Communal/Profiles/828"));

    }
}
