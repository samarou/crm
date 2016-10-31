package com.itechart.security.business.service;


import org.jsoup.nodes.Document;

public interface LinkedInService {

    Document getLinkedinPage(String url);
}
