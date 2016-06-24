package com.itechart.scraper.parser.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.scraper.model.SmgProfile;
import com.itechart.scraper.model.SmgProfileResponse;
import com.itechart.scraper.model.SmgSession;
import com.itechart.scraper.parser.Parser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class SmgProfileParser implements Parser {
    private static final Logger log = LoggerFactory.getLogger(SmgProfileParser.class);

    @Value("${smg.profile}")
    private String smgProfile;

    @Value("${smg.profile.password}")
    private String smgProfilePassword;

    @Override
    public SmgProfile parse(String profileId) throws IOException {
        log.debug("profile id = {}", profileId);
        SmgProfile smgProfile = null;
        try {
            Long sessionId = login();
            smgProfile = getProfile(sessionId, profileId);
        } catch (IOException e) {
            log.error("can't read SMG profile", e);
        }
        return smgProfile;
    }

    private Long login() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("config.properties");
        Properties properties = new Properties();
        properties.load(input);

        ObjectMapper mapper = new ObjectMapper();
        SmgSession smgSession = mapper.readValue(
            new URL(String.format(
                "https://smg.itechart-group.com/MobileServiceNew/MobileService.svc/Authenticate?username=%s&password=%s",
                properties.getProperty("smg.profile"),
                properties.getProperty("smg.profile.password"))),
            SmgSession.class);
        log.debug("SMG session. error code: {}, permission: {}, sessionId: {}",
            smgSession.getErrorCode(),
            smgSession.getPermission(),
            smgSession.getSessionId()
        );
        if (smgSession.getSessionId() == 0L) {
            throw new RuntimeException(smgSession.getErrorCode());
        }
        return smgSession.getSessionId();
    }

    private SmgProfile getProfile(Long sessionId, String profileId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SmgProfileResponse response = mapper.readValue(
            new URL(String.format(
                "https://smg.itechart-group.com/MobileServiceNew/MobileService.svc/GetEmployeeDetails?sessionId=%1d&profileId=%2s",
                sessionId,
                profileId)), SmgProfileResponse.class);
        if (!StringUtils.isEmpty(response.getErrorCode())) {
            throw new RuntimeException(response.getErrorCode());
        }
        return response.getProfile();
    }
}
