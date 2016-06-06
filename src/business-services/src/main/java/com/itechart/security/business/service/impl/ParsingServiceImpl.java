package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.CountryDao;
import com.itechart.security.business.model.dto.*;
import com.itechart.security.business.model.persistent.Country;
import com.itechart.security.business.service.ParsingService;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by anton.charnou on 23.05.2016.
 */
@Service
public class ParsingServiceImpl implements ParsingService {
    private static final Logger log = LoggerFactory.getLogger(ParsingServiceImpl.class);
    private static final String USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    private static final String REFFERRER = "http://www.google.com";

    @Autowired
    private CountryDao countryDao;

    @Override
    @Transactional
    public LinkedInContactDto parse(String profileUrl) {
        log.debug("profile url = {}", profileUrl);
        Document document;
        LinkedInContactDto contact = new LinkedInContactDto();
        try {
            URI uri = new URI(URLDecoder.decode(profileUrl, "UTF-8"));
            document = Jsoup.connect(uri.toASCIIString())
                    .userAgent(USER_AGENT)
                    .referrer(REFFERRER).get();
            contact.setFullName(getFullName(document));
            contact.setFirstName(getFirstName(document));
            contact.setLastName(getLastName(document));
            contact.setLocation(getLocationOrIndustry(document, this::locationFilter));
            contact.setIndustry(getLocationOrIndustry(document, this::industryFilter));
            contact.setSummary(getSummary(document));
            contact.setSkills(getSkills(document));
            contact.setSchools(getEducation(document));
            contact.setPhotoUrl(getPicture(document));
            contact.setLanguages(getLanguages(document));
            contact.setWorkplaces(getWorkplaces(document, this::jobFilter));
            contact.setProjects(getProjects(document));
            Set<AddressDto> addressDtoSet = new HashSet<>();
            addressDtoSet.add(getAddress(contact.getLocation()));
            contact.setAddresses(addressDtoSet);
        } catch (IOException | URISyntaxException e) {
            log.error("Can't get html page", profileUrl);
        }
        return contact;
    }


    private String getFullName(Document document) {
        return document.getElementById("name").text();
    }

    private List<String> splitNames(Document document) {
        return Arrays.asList(getFullName(document).split(" "));
    }


    private String getFirstName(Document document) {
        List<String> nameList = splitNames(document);
        return nameList.isEmpty() ? null : nameList.get(0);
    }

    private String getLastName(Document document) {
        List<String> nameList = splitNames(document);
        return nameList.size() <= 1 ? null : nameList.get(nameList.size() - 1);
    }

    private String getLocationOrIndustry(Document document, Predicate<Element> filter) {
        Element location = document.getElementById("demographics");
        return Objects.isNull(location) ? null : find(location.children(), filter);
    }

    private String getWork(Document document, Predicate<Element> filter) {
        Elements currentPosition = document.getElementsByAttribute("data-section");
        return currentPosition.isEmpty() ? null : find(currentPosition, filter);
    }

    private String getSummary(Document document) {
        Element summary = document.getElementById("summary");
        return Objects.isNull(summary) ? null : summary.getElementsByClass("description").text();
    }

    private Set<SkillDto> getSkills(Document document) {
        Element skillsTab = document.getElementById("skills");
        return Objects.isNull(skillsTab) ? new HashSet<>() : skillsTab.getElementsByClass("skill").stream()
                .map(ParsingServiceImpl::getSkillDto).collect(Collectors.toSet());
    }

    private static SkillDto getSkillDto(Element element){
        SkillDto dto = new SkillDto();
        dto.setName(element.text());
        return dto;
    }

    private Set<SchoolDto> getEducation(Document document) {
        Element schools = document.getElementById("education");
        return Objects.isNull(schools) ? new HashSet<>() : schools.getElementsByClass("school").stream()
                .map(element -> {
                    SchoolDto school = new SchoolDto();
                    school.setName(element.getElementsByClass("item-title").first().text());
                    school.setDegree(element.getElementsByClass("item-subtitle").text());
                    school.setDateRange(element.getElementsByClass("date-range").text());
                    return school;
                }).collect(Collectors.toSet());
    }


    private String getPicture(Document document) {
        Elements photos = document.getElementsByClass("photo");
        return photos.isEmpty() ? null : photos.attr("data-delayed-url");
    }

    private Set<LanguageDto> getLanguages(Document document) {
        Element languagesTabs = document.getElementById("languages");
        return Objects.isNull(languagesTabs) ? new HashSet<>() : languagesTabs.getElementsByClass("language").stream()
                .map(element -> {
                    LanguageDto language = new LanguageDto();
                    language.setName(element.getElementsByClass("name").text());
                    language.setLevel(element.getElementsByClass("proficiency").text());
                    return language;
                }).collect(Collectors.toSet());
    }

    private Set<WorkplaceDto> getWorkplaces(Document document, Predicate<Element> filter) {
        Element experienceTab = document.getElementById("experience");
        return Objects.isNull(experienceTab) ? new HashSet<>() : experienceTab.getElementsByClass("position").stream().filter(filter)
                .map(element -> {
                    WorkplaceDto workplace = new WorkplaceDto();
                    workplace.setPosition(element.getElementsByClass("item-title").text());
                    workplace.setName(element.getElementsByClass("item-subtitle").text());
                    String dateRange = element.getElementsByClass("date-range").text();
                    List<Date> dateList = getDateList(dateRange);
                    workplace.setStartDate(getDate(dateList, 0));
                    workplace.setEndDate(getDate(dateList, 1));
                    workplace.setComment(element.getElementsByClass("description").text());
                    return workplace;
                }).collect(Collectors.toSet());
    }


    private Set<ProjectDto> getProjects(Document document) {
        Element projectTab = document.getElementById("projects");
        return Objects.isNull(projectTab) ? new HashSet<>() : projectTab.getElementsByClass("project").stream()
                .map(element -> {
                    ProjectDto project = new ProjectDto();
                    project.setName(element.getElementsByClass("item-title").text());
                    project.setContributors(element.getElementsByClass("contributors").text());
                    project.setDate(element.getElementsByClass("date-range").text());
                    project.setDesc(element.getElementsByClass("description").text());
                    return project;
                }).collect(Collectors.toSet());
    }


    private String find(Elements elements, Predicate<Element> filter) {
        Optional<Element> optional = elements.stream().filter(filter).findFirst();
        return optional.isPresent() ? optional.get().text() : null;
    }

    private Date getDate(List<Date> dateList, int position) {
        return dateList.size() > position ? dateList.get(position) : null;
    }

    private AddressDto getAddress(String location) {
        AddressDto address = new AddressDto();
        List<String> addressStrings = parseLocation(location);

        if (!addressStrings.isEmpty()) {
            String countryName = addressStrings.get(addressStrings.size() - 1);
            Country country = countryDao.getByName(countryName);
            if (!Objects.isNull(country)) {
                address.setCountry(
                        countryDao.getByName(countryName)
                                .getId());
                if (addressStrings.size() == 2) address.setRegion(addressStrings.get(0));
                if (addressStrings.size() == 3) {
                    address.setRegion(addressStrings.get(1));
                    address.setCity(addressStrings.get(0));
                }
            } else {
                if (addressStrings.size() == 2) {
                    address.setRegion(addressStrings.get(1));
                    address.setCity(addressStrings.get(0));
                }
            }
        }

        return address;
    }

    private List<String> parseLocation(String location) {
        return Arrays.asList(location.split(", "));
    }


    private List<Date> getDateList(String dataRange) {
        return parseDataRange(dataRange)
                .stream().map(this::parseDateString)
                .collect(Collectors.toList());
    }

    private List<String> parseDataRange(String dateRange) {
        String[] startDate = dateRange.split(" – | \\(.+\\)");
        return Arrays.asList(startDate);
    }

    private Date parseDateString(String dateString) {
        if (dateString.equals("Present")) return null;
        Date date = null;
        Locale dflt = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
        try {
            date = DateUtils.parseDate(dateString, "MMMM yyyy", "yyyy");
        } catch (ParseException e) {
            log.error("Can't parse date string = {}", dateString);
        } finally {
            Locale.setDefault(dflt);
        }
        return date;
    }

    private boolean locationFilter(Element element) {
        return element.tag() == Tag.valueOf("dd") && element.hasClass("adr");
    }

    private boolean industryFilter(Element element) {
        return element.tag() == Tag.valueOf("dd") && !element.hasClass("adr");
    }

    private boolean currentWorkFilter(Element element) {
        return element.attr("data-section").equals("currentPositionsDetails");
    }

    private boolean previousWorkFilter(Element element) {
        return element.attr("data-section").equals("pastPositionsDetails");
    }

    private boolean jobFilter(Element element) {
        return element.attr("data-section").equals("currentPositionsDetails") ||
                element.attr("data-section").equals("pastPositionsDetails");
    }

}
