package com.itechart.scrapper.model.crm.contact;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
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
}
