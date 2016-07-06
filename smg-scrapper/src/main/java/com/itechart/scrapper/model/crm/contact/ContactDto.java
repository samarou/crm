package com.itechart.scrapper.model.crm.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.Set;

import static com.itechart.scrapper.ScrapperUtils.addElementsToSet;
import static org.springframework.util.StringUtils.isEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private Date dateOfBirth;

    private Boolean isMale;

    private String nationality;

    private String photoUrl;

    private String industry;

    private Set<MessengerAccountDto> messengers;

    private Set<TelephoneDto> telephones;

    private Set<EmailDto> emails;

    public boolean addFields(ContactDto another) {
        boolean isEdited = false;
        if (isEmpty(getId())) {
            setId(another.getId());
        }
        if (isEmpty(getFirstName())) {
            setFirstName(another.getFirstName());
            isEdited = true;
        }
        if (isEmpty(getLastName())) {
            setLastName(another.getLastName());
            isEdited = true;
        }
        if (isEmpty(getPatronymic())) {
            setPatronymic(another.getPatronymic());
            isEdited = true;
        }
        if (isEmpty(getNationality())) {
            setNationality(another.getNationality());
        }
        if (isEmpty(getPhotoUrl())) {
            setPhotoUrl(another.getPhotoUrl());
            isEdited = true;
        }
        if (isEmpty(getIndustry())) {
            setIndustry(another.getIndustry());
            isEdited = true;
        }
        setDateOfBirth(getDateOfBirth() != null ? getDateOfBirth() : another.getDateOfBirth());
        setIsMale(getIsMale() != null ? getIsMale() : another.getIsMale());
        if (addElementsToSet(messengers, another.getMessengers())) {
            isEdited = true;
        }
        if (addElementsToSet(telephones, another.getTelephones())) {
            isEdited = true;
        }
        if (addElementsToSet(emails, another.getEmails())) {
            isEdited = true;
        }
        return isEdited;
    }
}
