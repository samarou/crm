package com.itechart.scrapper.model.smg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itechart.scrapper.model.crm.SecuredUserDto;
import com.itechart.scrapper.model.crm.contact.ContactDto;
import com.itechart.scrapper.model.crm.contact.EmailDto;
import com.itechart.scrapper.model.crm.contact.MessengerAccountDto;
import com.itechart.scrapper.model.crm.contact.TelephoneDto;
import com.itechart.scrapper.model.smg.response.SmgProfileResponse;
import com.itechart.scrapper.model.smg.response.SmgProfilesResponse;
import com.itechart.scrapper.model.smg.response.SmgResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmgProfile extends SmgShortProfile {

    private static final String SMG_REST_URL = "https://smg.itechart-group.com/MobileServiceNew/MobileService.svc";
    private static final Long SKYPE_ID = 1L;

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
    public String Rank;
    public String Skype;
    public String Group;

    public static SmgProfile getProfile(Integer sessionId, Integer profileId) throws IOException {
        URL url = new URL(String.format(
            SMG_REST_URL + "/GetEmployeeDetails?sessionId=%d&profileId=%d",
            sessionId,
            profileId));
        SmgProfileResponse response = SmgResponse.getResponse(url, SmgProfileResponse.class);
        return response.getProfile();
    }

    public static List<Integer> getProfileIDsByDeptId(Integer sessionId, Integer departmentId) throws IOException, InterruptedException {
        URL url = new URL(String.format(
            SMG_REST_URL + "/GetEmployeesByDeptId?sessionId=%d&departmentId=%d",
            sessionId,
            departmentId));
        SmgProfilesResponse response = SmgResponse.getResponse(url, SmgProfilesResponse.class);
        return response.getProfileIds();
    }

    public static List<Integer> getProfileIDs(Integer sessionId) throws IOException, InterruptedException {
        URL url = new URL(String.format(
            SMG_REST_URL + "/GetAllEmployees?sessionId=%d",
            sessionId));
        SmgProfilesResponse response = SmgResponse.getResponse(url, SmgProfilesResponse.class);
        return response.getProfileIds();
    }

    public static List<SmgProfile> getAllProfiles(Integer sessionId) throws IOException {
        URL url = new URL(String.format(
            SMG_REST_URL + "/GetEmployeeDetailsListByDeptId?sessionId=%d&departmentId=0",
            sessionId));
        SmgProfilesResponse response = SmgResponse.getResponse(url, SmgProfilesResponse.class);
        return response.getProfiles();
    }

    public SecuredUserDto convertToUser() {
        SecuredUserDto user = new SecuredUserDto();
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        user.setPassword(getDomenName());
        user.setUserName(getDomenName());
        user.setEmail(getEmail());
        user.setActive(true);
        user.setGroups(new HashSet<>());
        return user;
    }

    public ContactDto convertToContact() {
        ContactDto contact = new ContactDto();
        contact.setFirstName(getFirstName());
        contact.setLastName(getLastName());
        contact.setFirstName(getFirstName());
        contact.setLastName(getLastName());
        contact.setPatronymic(getMiddleName());
        contact.setPhotoUrl(getImage());
        contact.setIndustry("Information Technologies");

        Set<TelephoneDto> telephoneDtos = new HashSet<>();
        Set<EmailDto> emailDtos = new HashSet<>();
        Set<MessengerAccountDto> messengerAccountDtos = new HashSet<>();

        if (nonNull(getPhone())) {
            telephoneDtos.add(new TelephoneDto(null, getPhone(), "WORK"));
        }
        if (nonNull(getEmail())) {
            emailDtos.add(new EmailDto(null, getEmail(), "WORK"));
        }
        if (nonNull(getSkype())) {
            messengerAccountDtos.add(new MessengerAccountDto(null, SKYPE_ID, getSkype()));
        }
        contact.setTelephones(telephoneDtos);
        contact.setEmails(emailDtos);
        contact.setMessengers(messengerAccountDtos);
        return contact;
    }
}