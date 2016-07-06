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

    public void addFields(ContactDto another) {
        setId(!isEmpty(getId()) ? getId() : another.getId());
        setFirstName(!isEmpty(getFirstName()) ? getFirstName() : another.getFirstName());
        setLastName(!isEmpty(getLastName()) ? getLastName() : another.getLastName());
        setPatronymic(!isEmpty(getPatronymic()) ? getPatronymic() : another.getPatronymic());
        setDateOfBirth(!isEmpty(getDateOfBirth()) ? getDateOfBirth() : another.getDateOfBirth());
        setIsMale(!isEmpty(getIsMale()) ? getIsMale() : another.getIsMale());
        setNationality(!isEmpty(getNationality()) ? getNationality() : another.getNationality());
        setPhotoUrl(!isEmpty(getPhotoUrl()) ? getPhotoUrl() : another.getPhotoUrl());
        setIndustry(!isEmpty(getIndustry()) ? getIndustry() : another.getIndustry());
        addElementsToSet(messengers, another.getMessengers());
        addElementsToSet(telephones, another.getTelephones());
        addElementsToSet(emails, another.getEmails());
    }
}
