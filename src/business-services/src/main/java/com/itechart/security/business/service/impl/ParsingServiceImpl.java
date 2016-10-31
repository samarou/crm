package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.CountryDao;
import com.itechart.security.business.model.dto.*;
import com.itechart.security.business.model.persistent.Country;
import com.itechart.security.business.service.LinkedInService;
import com.itechart.security.business.service.ParsingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private LinkedInService linkedInService;

    @Override
    @Transactional
    public LinkedInContactDto parse(String profileUrl) {
        log.debug("profile url = {}", profileUrl);
        Document document;
        LinkedInContactDto contact = new LinkedInContactDto();
        try {
            document = linkedInService.getLinkedinPage(profileUrl);
            if (Objects.isNull(document)) {
                return contact;
            }
            contact.setFullName(getFullName(document));
            contact.setFirstName(getFirstName(document));
            contact.setLastName(getLastName(document));
            contact.setLocation(getLocation(document));
            contact.setIndustry(getIndustry(document));
            contact.setSummary(getSummary(document));
            contact.setSkills(getSkills(document));
            contact.setEducations(getUniversityEducation(document));
            contact.setPhotoUrl(getPicture(document));
            contact.setLanguages(getLanguages(document));
            contact.setWorkplaces(getWorkplaces(document, this::jobFilter));
            contact.setProjects(getProjects(document));
            Set<AddressDto> addressDtoSet = new HashSet<>();
            addressDtoSet.add(getAddress(contact.getLocation()));
            contact.setAddresses(addressDtoSet);

        } catch (Exception e) {
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

    private String getSummary(Document document) {
        Element summary = document.getElementById("summary-item-view");
        return Objects.isNull(summary) ? null : summary.getElementsByClass("description").text();
    }

    private String getLocation(Document document) {
        Element element = document.getElementById("location-container");
        return Objects.isNull(element) ? null : element.getElementsByClass("locality").text();
    }

    private String getIndustry(Document document) {
        Element element = document.getElementById("location-container");
        return Objects.isNull(element) ? null : element.getElementsByClass("industry").text();
    }

    private Set<SkillDto> getSkills(Document document) {
        Element skillsTab = document.getElementById("skills-item-view");
        return Objects.isNull(skillsTab) ? new HashSet<>() : skillsTab.getElementsByClass("endorse-item-name").stream()
                .map(ParsingServiceImpl::getSkillDto).collect(Collectors.toSet());
    }

    private static SkillDto getSkillDto(Element element) {
        SkillDto dto = new SkillDto();
        dto.setName(element.text());
        return dto;
    }

    private Set<UniversityEducationDto> getUniversityEducation(Document document) {
        Element schools = document.getElementById("background-education");
        return Objects.isNull(schools) ? new HashSet<>() : schools.getElementsByClass("education").stream()
                .map(element -> {
                    UniversityEducationDto school = new UniversityEducationDto();
                    school.setName(element.getElementsByClass("summary").text());
                    school.setSpeciality(element.getElementsByClass("major").text());
                    List<Date> dates = getDateList(element.getElementsByClass("education-date").text());
                    school.setStartDate(getDate(dates, 0));
                    school.setEndDate(getDate(dates, 1));
                    school.setType("SIMPLE");
                    return school;
                }).collect(Collectors.toSet());
    }

    private String getPicture(Document document) {
        Elements photos = document.getElementsByClass("profile-picture");
        return photos.isEmpty() ? null : photos.select("img").attr("src");
    }

    private Set<LanguageDto> getLanguages(Document document) {
        Element languagesTabs = document.getElementById("languages-view");
        return Objects.isNull(languagesTabs) ? new HashSet<>() : languagesTabs.getElementsByClass("section-item").stream()
                .map(element -> {
                    LanguageDto language = new LanguageDto();
                    language.setName(element.select("span").first().text());
                    language.setLevel(element.getElementsByClass("languages-proficiency").text());
                    return language;
                }).collect(Collectors.toSet());
    }

    private Set<WorkplaceDto> getWorkplaces(Document document, Predicate<Element> filter) {
        Element experienceTab = document.getElementById("background-experience");
        return Objects.isNull(experienceTab) ? new HashSet<>() : experienceTab.getElementsByClass("editable-item").stream()
                .map(element -> {
                    WorkplaceDto workplace = new WorkplaceDto();
                    workplace.setPosition(element.getElementsByTag("h4").first().text());
                    workplace.setName(element.getElementsByClass("new-miniprofile-container").text());
                    String dateRange = element.getElementsByClass("experience-date-locale").text();
                    List<Date> dateList = getDateList(dateRange);
                    workplace.setStartDate(getDate(dateList, 0));
                    workplace.setEndDate(getDate(dateList, 1));
                    workplace.setComment(element.getElementsByClass("description").html());
                    return workplace;
                }).collect(Collectors.toSet());
    }


    private Set<ProjectDto> getProjects(Document document) {
        Element projectTab = document.getElementById("background-projects");
        return Objects.isNull(projectTab) ? new HashSet<>() : projectTab.getElementsByClass("editable-item").stream()
                .map(element -> {
                    ProjectDto project = new ProjectDto();
                    project.setName(element.getElementsByClass("summary").select("span").first().text());
                    project.setContributors(element.getElementsByClass("contributors").text());
                    project.setDate(element.getElementsByClass("projects-date").text());
                    project.setDesc(element.getElementsByClass("description").text());
                    return project;
                }).collect(Collectors.toSet());
    }


    private Date getDate(List<Date> dateList, int position) {
        return dateList.size() > position ? dateList.get(position) : null;
    }


    private AddressDto getAddress(String location) {
        AddressDto address = new AddressDto();
        List<String> addressStrings = parseLocation(location);

        if (!addressStrings.isEmpty()) {
            String countryName = addressStrings.get(addressStrings.size() - 1);
            countryName = convertCoutryNameToEnglish(countryName);
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
        return StringUtils.isBlank(location) ? new ArrayList<>()
                : Arrays.asList(location.split(", "));
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
        String locale = "ru";
        if (dateString.equals("Present")) return null;
        Date date = null;
        Locale dflt = Locale.getDefault();
        Locale.setDefault(new Locale(locale));
        try {
            date = DateUtils.parseDate(dateString, "MMMM yyyy", "yyyy");
        } catch (ParseException e) {
            log.error("Can't parse date string = {}", dateString);
        } finally {
            Locale.setDefault(dflt);
        }
        return date;
    }

    private boolean jobFilter(Element element) {
        return element.attr("data-section").equals("currentPositionsDetails") ||
                element.attr("data-section").equals("pastPositionsDetails");
    }

    private String convertCoutryNameToEnglish(String country) {
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale locale = new Locale("", countryCode);
            if (country.equals(locale.getDisplayCountry(new Locale("ru")))) {
                return locale.getDisplayCountry(new Locale("en"));
            }
        }
        return country;
    }

}
