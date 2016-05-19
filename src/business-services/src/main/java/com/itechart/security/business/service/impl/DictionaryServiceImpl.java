package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.CountryDao;
import com.itechart.security.business.model.dto.CountryDto;
import com.itechart.security.business.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.itechart.security.business.model.dto.utils.DtoConverter.convertCountries;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private CountryDao countryDao;

    @Override
    public List<CountryDto> getCountries() {
        return convertCountries(countryDao.loadAll());
    }
}
