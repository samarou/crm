package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.Country;

import java.util.List;

public interface CountryDao {
    List<Country> loadAll();

    Country get(Long id);
}
