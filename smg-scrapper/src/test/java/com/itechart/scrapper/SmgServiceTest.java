package com.itechart.scrapper;

import com.itechart.scrapper.utils.SmgParser;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SmgServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SmgServiceTest.class);

    @Test
    public void login(){
       new SmgParser();
    }

    @Test
    public void parseProfile() throws IOException, InterruptedException {
        SmgParser smgParser = new SmgParser();
        int i=0;
        for(;i<1000;i++){
            smgParser.getProfile(828);
            System.out.println(i+1);
            Thread.sleep(2000);
        }
        Assert.assertSame(i,1999);
    }
}
