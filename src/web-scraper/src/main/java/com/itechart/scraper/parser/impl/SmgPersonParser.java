package com.itechart.scraper.parser.impl;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.itechart.scraper.model.Person;
import com.itechart.scraper.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SmgPersonParser implements Parser {
    private static final Logger log = LoggerFactory.getLogger(SmgPersonParser.class);

    @Override
    public Person parse(String profileUrl) throws IOException {
        log.debug("profile url = {}", profileUrl);
        Person person;
        try (final WebClient webClient = new WebClient()) {
            login(webClient);
            HtmlPage page = webClient.getPage(profileUrl);
            person = parse(page);

        }
        return person;
    }


    private Page login(WebClient webClient) throws IOException {
        webClient.getOptions().setUseInsecureSSL(true);
        HtmlPage page = webClient.getPage("https://smg.itechart-group.com/Login.aspx");
        HtmlForm form = (HtmlForm) page.getElementById("form");

        HtmlTextInput loginInput = form.getInputByName("Username");
        HtmlPasswordInput passwordInput = form.getInputByName("Password");
        HtmlButton loginButton = (HtmlButton) page.getElementById("btnLogin");
        loginInput.setValueAttribute("anton.charnou");
        passwordInput.setValueAttribute("freeman86");
        return loginButton.click();
    }

    private Person parse(HtmlPage page) {
        Person person = new Person();
        person.setFirstName(getField("ctl00_tbxNameEngSecond", page));
        person.setLastName(getField("ctl00_tbxNameEngFirst", page));
        person.setMobile(getField("ctl00_tbxMobile", page));
        person.setSkype(getField("ctl00_tbxSkype", page));
        return person;
    }

    private String getField(String inputId, HtmlPage page) {
        HtmlInput input = (HtmlInput) page.getElementById(inputId);
        return input.getValueAttribute();
    }
}
