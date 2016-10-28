package com.itechart.security.business.service.impl;


import com.itechart.security.business.service.LinkedInService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

public class LinkedInServiceImpl implements LinkedInService {

    private static final Logger log = LoggerFactory.getLogger(LinkedInServiceImpl.class);

    private String linkedinUserLogin;
    private String linkedinUserPassword;

    @Override
    public Document getLinkedinPage(String url) {

        String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";

        Document document = null;
        try {
            URI uri = new URI(URLDecoder.decode(url, "UTF-8"));

            Connection.Response response = signin();

            document = Jsoup.connect(uri.toASCIIString())
                    .cookies(response.cookies())
                    .userAgent(USER_AGENT)
                    .timeout(60 * 10000)
                    .get();
        } catch (IOException | URISyntaxException e) {
            log.error("Can't get html page", url);
        }
        return document;
    }

    private Connection.Response signin(){

        Connection.Response response = null;

        String LOGIN_URL = "https://www.linkedin.com/uas/login?goback=&trk=hb_signin";

        try {
            response = Jsoup
                    .connect(LOGIN_URL)
                    .method(Connection.Method.GET)
                    .execute();

            Document responseDocument = response.parse();

            Element loginCsrfParam = responseDocument
                    .select("input[name=loginCsrfParam]")
                    .first();

            response = Jsoup.connect("https://www.linkedin.com/uas/login-submit")
                    .cookies(response.cookies())
                    .data("loginCsrfParam", loginCsrfParam.attr("value"))
                    .data("session_key", linkedinUserLogin)
                    .data("session_password", linkedinUserPassword)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .execute();

        }catch (IOException e){
            log.error("Can't get html page", LOGIN_URL);
        }
        return response;
    }

    public void setLinkedinUserLogin(String linkedinUseLogin) {
        this.linkedinUserLogin = linkedinUseLogin;
    }

    public void setLinkedinUserPassword(String linkedinUserPassword) {
        this.linkedinUserPassword = linkedinUserPassword;
    }
}

