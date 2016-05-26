package com.itechart.security.business.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class ContactDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private HistoryEntryDto history;

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

    public HistoryEntryDto getHistory() {
        return history;
    }

    public void setHistory(HistoryEntryDto history) {
        this.history = history;
    }
}
