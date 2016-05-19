package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.CountryDto;

import java.util.List;

public interface DictionaryService {
    List<CountryDto> getCountries();
}
