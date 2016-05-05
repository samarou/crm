package com.itechart.security.business.model.dto;

import com.itechart.security.business.model.persistent.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.Set;

@Getter
@Setter
public class ContactDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private Date dateOfBirth;

    private Boolean isMale;

    private String nationality;

    private String photoUrl;

    private Set<OrderDto> orders;

    private Set<MessengerAccountDto> messengers;

    private Set<SocialNetworkAccountDto> socialNetworks;

    private Set<TelephoneDto> telephones;

    private Set<AddressDto> addresses;

    private Set<WorkplaceDto> workplaces;

    private Set<EmailDto> emails;
}
