package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.CountryDao;
import com.itechart.security.business.dao.MessengerDao;
import com.itechart.security.business.dao.SocialNetworkDao;
import com.itechart.security.business.model.dto.CountryDto;
import com.itechart.security.business.model.dto.MessengerDto;
import com.itechart.security.business.model.dto.SocialNetworkDto;
import com.itechart.security.business.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itechart.security.business.model.dto.utils.DtoConverter.convertCountries;
import static com.itechart.security.business.model.dto.utils.DtoConverter.convertMessengers;
import static com.itechart.security.business.model.dto.utils.DtoConverter.convertSocialNetworks;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private MessengerDao messengerDao;

    @Autowired
    private SocialNetworkDao socialNetworkDao;

    @Override
    @Transactional
    public List<CountryDto> getCountries() {
        return convertCountries(countryDao.loadAll());
    }

    @Override
    @Transactional
    public List<SocialNetworkDto> getSocialNetworks() {
        return convertSocialNetworks(socialNetworkDao.loadAll());
    }

    @Override
    @Transactional
    public List<MessengerDto> getMessengers() {
        return convertMessengers(messengerDao.loadAll());
    }
}
