package com.itechart.scrapper.utils;

import com.itechart.scrapper.model.smg.SmgDepartment;
import com.itechart.scrapper.model.smg.SmgProfile;
import com.itechart.scrapper.model.smg.response.SmgDepartmentsResponse;
import com.itechart.scrapper.model.smg.response.SmgProfileResponse;
import com.itechart.scrapper.model.smg.response.SmgProfilesResponse;
import com.itechart.scrapper.model.smg.response.SmgSessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.itechart.scrapper.model.smg.response.SmgResponse.getResponse;
import static com.itechart.scrapper.utils.ScrapperProperties.getSmgUser;
import static com.itechart.scrapper.utils.ScrapperProperties.getSmgUserPassword;

public class SmgParser {

    private static final String SMG_REST_URL = "https://smg.itechart-group.com/MobileServiceNew/MobileService.svc";

    private static final Logger log = LoggerFactory.getLogger(SmgParser.class);
    private static Integer smgSessionId;

    public SmgParser() {
        try {
            smgSessionId = getSession(
                getSmgUser(),
                getSmgUserPassword()
            );
        } catch (IOException e) {
            log.error("can't set SMG session", e);
        }
    }

    public SmgProfile getProfile(Integer profileId) throws IOException {
        log.debug("profile id = {}", profileId);
        SmgProfile smgProfile;
        smgProfile = getProfile(smgSessionId, profileId);
        return smgProfile;
    }

    public List<SmgProfile> getAllProfiles() throws IOException {
        return getAllProfiles(smgSessionId);
    }

    public List<SmgDepartment> getDepartments() throws IOException {
        List<SmgDepartment> smgDepartments;
        smgDepartments = getAllDepartments(smgSessionId);
        return smgDepartments;
    }

    private static Integer getSession(String userName, String password) throws IOException {
        SmgSessionResponse smgSession = getResponse(
            new URL(String.format(
                SMG_REST_URL + "/Authenticate?username=%s&password=%s",
                userName, password)),
            SmgSessionResponse.class);
        log.debug("SMG sessionId: {}", smgSession.getSessionId());
        return smgSession.getSessionId();
    }

    private static SmgProfile getProfile(Integer sessionId, Integer profileId) throws IOException {
        URL url = new URL(String.format(
            SMG_REST_URL + "/GetEmployeeDetails?sessionId=%d&profileId=%d",
            sessionId,
            profileId));
        SmgProfileResponse response = getResponse(url, SmgProfileResponse.class);
        return response.getProfile();
    }

    private static List<SmgProfile> getAllProfiles(Integer sessionId) throws IOException {
        URL url = new URL(String.format(
            SMG_REST_URL + "/GetEmployeeDetailsListByDeptId?sessionId=%d&departmentId=0",
            sessionId));
        SmgProfilesResponse response = getResponse(url, SmgProfilesResponse.class);
        return response.getProfiles();
    }

    private static List<SmgDepartment> getAllDepartments(Integer sessionId) throws IOException {
        URL url = new URL(String.format(
            SMG_REST_URL + "/GetAllDepartments?sessionId=%s",
            sessionId));
        SmgDepartmentsResponse response = getResponse(url, SmgDepartmentsResponse.class);
        return response.getDepts();
    }
}
