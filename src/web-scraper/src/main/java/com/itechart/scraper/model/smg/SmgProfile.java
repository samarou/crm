package com.itechart.scraper.model.smg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itechart.scraper.model.smg.response.SmgProfileResponse;
import com.itechart.scraper.model.smg.response.SmgProfilesResponse;
import com.itechart.scraper.model.smg.response.SmgResponse;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmgProfile {
    public Integer DeptId;
    public String DomenName;
    public String Email;
    public String FirstName;
    public String FirstNameEng;
    public String Image;
    public String LastName;
    public String LastNameEng;
    public String MiddleName;
    public String Phone;
    public Integer ProfileId;
    public String Rank;
    public String Skype;
    public String Group;

    public static SmgProfile getProfile(Integer sessionId, Integer profileId) throws IOException {
        URL url = new URL(String.format(
            "https://smg.itechart-group.com/MobileServiceNew/MobileService.svc/GetEmployeeDetails?sessionId=%d&profileId=%d",
            sessionId,
            profileId));
        SmgProfileResponse response = SmgResponse.getResponse(url, SmgProfileResponse.class);
        return response.getProfile();
    }

    public static List<SmgProfile> getProfilesByDeptId(Integer sessionId, Integer departmentId) throws IOException, InterruptedException {
        URL url = new URL(String.format(
            "https://smg.itechart-group.com/MobileServiceNew/MobileService.svc/GetEmployeesByDeptId?sessionId=%d&departmentId=%d",
            sessionId,
            departmentId));
        SmgProfilesResponse response = SmgResponse.getResponse(url, SmgProfilesResponse.class);
        return getAllProfiles(sessionId, response.getProfileIds());
    }

    private static List<SmgProfile> getAllProfiles(Integer sessionId, List<Integer> profileIds) throws IOException, InterruptedException {
        List<SmgProfile> smgProfiles = new ArrayList<>();
        for (Integer profileId : profileIds) {
            smgProfiles.add(getProfile(sessionId, profileId));
        }
        return smgProfiles;
    }
}