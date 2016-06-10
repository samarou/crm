package com.itechart.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Created by anton.charnou on 27.05.2016.
 */
public class GooglePageCrawler implements Crawler {
    private final String SEARCH_QUERY = "site:www.linkedin.com See who you know in common";
    private String fileName = "./google_profiles.txt";


    public void crawl(String fileName) throws IOException {
        if (!Objects.isNull(fileName)) this.fileName = fileName;
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://www.google.by");
            final HtmlForm form = page.getFormByName("f");

            final HtmlButton button = form.getButtonsByName("btnG").get(0);
            final HtmlTextInput textField = form.getInputByName("q");

            textField.setValueAttribute(SEARCH_QUERY);

            HtmlPage resultPage = button.click();
            List<String> links = getLinks(resultPage, 100000000);
            links.stream().forEach(System.out::println);
        }
    }

    private List<String> getLinks(HtmlPage searchPage, int linksNumber) throws IOException {
        int pageNumber = linksNumber / 10;
        List<String> bufferLinkList = new ArrayList<>();
        bufferLinkList.addAll(getLinksFromPage(searchPage));
        for (int i = 0; i < pageNumber; i++) {
            System.out.println("iterate");
            DomElement button = searchPage.getElementById("pnnext");
            if (Objects.isNull(button)) return bufferLinkList;
            searchPage = button.click();
            List<String> strings = getLinksFromPage(searchPage);
            bufferLinkList.addAll(strings);
            Files.write(Paths.get(this.fileName), strings, APPEND, CREATE);
        }
        return bufferLinkList;
    }

    private List<String> getLinksFromPage(HtmlPage resultPage) {
        final DomElement resultDiv = resultPage.getHtmlElementById("rso");
        final List<?> links = resultDiv.getByXPath("//h3[@class='r']");
        return links.stream()
                .map(link -> ((HtmlElement) link).getFirstChild().getAttributes().getNamedItem("href").getTextContent())
                .collect(Collectors.toList());
    }
}
