package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.CountryDto;
import com.itechart.security.business.model.dto.SocialNetworkDto;

import java.util.List;

public interface DictionaryService {
    List<CountryDto> getCountries();

    List<SocialNetworkDto> getSocialNetworks();
}
