package com.itechart.scrapper.model.smg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itechart.scrapper.model.crm.SecuredUserDto;
import com.itechart.scrapper.model.crm.contact.ContactDto;
import com.itechart.scrapper.model.crm.contact.EmailDto;
import com.itechart.scrapper.model.crm.contact.MessengerAccountDto;
import com.itechart.scrapper.model.crm.contact.TelephoneDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.StringUtils.isEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SmgProfile extends SmgShortProfile {

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
    public String Position;

    public SecuredUserDto convertToUser() {
        SecuredUserDto user = new SecuredUserDto();
        user.setFirstName(getFirstName());
        user.setLastName(getLastName());
        user.setPassword(getDomenName());
        user.setUserName(getDomenName());
        user.setEmail(getEmail());
        user.setActive(true);
        user.setGroups(new HashSet<>());
        user.setAcls(new HashSet<>());
        user.setRoles(new HashSet<>());
        return user;
    }

    public ContactDto convertToContact() {
        ContactDto contact = new ContactDto();
        if (!isEmpty(getFirstName())) {
            contact.setFirstName(getFirstName());
        } else {
            contact.setFirstName(getFirstNameEng());
        }
        if (!isEmpty(getLastName())) {
            contact.setLastName(getLastName());
        } else {
            contact.setLastName(getLastNameEng());
        }
        contact.setFirstName(getFirstName());
        contact.setLastName(getLastName());
        contact.setPatronymic(getMiddleName());
        contact.setPhotoUrl(getImage());
        contact.setIndustry("Information Technologies");

        if (!isEmpty(getPhone())) {
            Set<TelephoneDto> telephoneDtos = new HashSet<>();
            telephoneDtos.add(new TelephoneDto(null, getPhone(), "WORK"));
            contact.setTelephones(telephoneDtos);
        }
        if (!isEmpty(getEmail())) {
            Set<EmailDto> emailDtos = new HashSet<>();
            emailDtos.add(new EmailDto(null, getEmail(), "WORK"));
            contact.setEmails(emailDtos);
        }
        if (!isEmpty(getSkype())) {
            Set<MessengerAccountDto> messengerAccountDtos = new HashSet<>();
            messengerAccountDtos.add(new MessengerAccountDto(null, SKYPE_ID, getSkype()));
            contact.setMessengers(messengerAccountDtos);
        }
        return contact;
    }

    public Boolean isDepartmentManager() {
        String rank = getRank();
        return rank != null && rank.contains("Department Manager");
    }
}