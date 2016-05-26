package com.itechart.security.business.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DictionaryDto {
    private List<CountryDto> countries;

    private List<SocialNetworkDto> socialNetworks;

    private List<MessengerDto> messengers;

    private List<EmailTypeDto> emailTypes;

    private List<TelephoneTypeDto> telephoneTypes;
}
