package com.itechart.scrapper.parser.impl;

import com.itechart.scrapper.model.smg.SmgDepartment;
import com.itechart.scrapper.model.smg.SmgProfile;
import com.itechart.scrapper.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static com.itechart.scrapper.model.smg.response.SmgSessionResponse.getSession;

public class SmgParser implements Parser {
    private static final Logger log = LoggerFactory.getLogger(SmgParser.class);
    private static Integer smgSessionId;
    private Properties properties;

    public SmgParser() {
        Properties prop = getProperties();
        try {
            smgSessionId = getSession(
                prop.getProperty("smg.profile"),
                prop.getProperty("smg.profile.password")
            );
        } catch (IOException e) {
            log.error("can't set SMG session", e);
        }
    }

    @Override
    public Properties getProperties() {
        if (properties == null) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream input = classLoader.getResourceAsStream("config.properties");
                properties = new Properties();
                properties.load(input);
            } catch (IOException e) {
                log.error("can't load properties for SMG parser", e);
            }
        }
        return properties;
    }

    @Override
    public SmgProfile getProfile(Integer profileId) throws IOException {
        log.debug("profile id = {}", profileId);
        SmgProfile smgProfile;
        smgProfile = SmgProfile.getProfile(smgSessionId, profileId);
        return smgProfile;
    }

    @Override
    public List<Integer> getProfileIDsByDeptId(Integer deptId) throws IOException, InterruptedException {
        log.debug("departmentId = {}", deptId);
        return SmgProfile.getProfileIDsByDeptId(smgSessionId, deptId);
    }

    @Override
    public List<Integer> getProfileIDs() throws IOException, InterruptedException {
        return SmgProfile.getProfileIDs(smgSessionId);
    }

    @Override
    public List<SmgDepartment> getDepartments() throws IOException {
        List<SmgDepartment> smgDepartments;
        smgDepartments = SmgDepartment.getAllDepartments(smgSessionId);
        return smgDepartments;
    }

    @Override
    public List<SmgProfile> getAllProfiles() throws IOException {
        return SmgProfile.getAllProfiles(smgSessionId);
    }
}
