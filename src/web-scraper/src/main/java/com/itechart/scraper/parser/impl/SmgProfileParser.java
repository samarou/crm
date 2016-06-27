package com.itechart.scraper.parser.impl;

import com.itechart.scraper.model.smg.SmgDepartment;
import com.itechart.scraper.model.smg.SmgProfile;
import com.itechart.scraper.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static com.itechart.scraper.model.smg.response.SmgSessionResponse.getSession;

public class SmgProfileParser implements Parser {
    private static final Logger log = LoggerFactory.getLogger(SmgProfileParser.class);
    private static Integer sessionId;

    public SmgProfileParser() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("config.properties");
        Properties properties = new Properties();
        try {
            properties.load(input);
            sessionId = getSession(
                properties.getProperty("smg.profile"),
                properties.getProperty("smg.profile.password")
            );
        } catch (IOException e) {
            log.error("cannot instantiate SMG parser", e);
        }
    }

    @Override
    public SmgProfile getProfile(Integer profileId) throws IOException {
        log.debug("profile id = {}", profileId);
        SmgProfile smgProfile;
        smgProfile = SmgProfile.getProfile(sessionId, profileId);
        return smgProfile;
    }

    @Override
    public List<SmgProfile> getProfilesByDeptId(Integer deptId) throws IOException, InterruptedException {
        log.debug("departmentId = {}", deptId);
        List<SmgProfile> smgProfiles;
        smgProfiles = SmgProfile.getProfilesByDeptId(sessionId, deptId);
        return smgProfiles;
    }

    @Override
    public List<SmgDepartment> getDepartments() throws IOException {
        List<SmgDepartment> smgDepartments;
        smgDepartments = SmgDepartment.getAllDepartments(sessionId);
        return smgDepartments;
    }


}
