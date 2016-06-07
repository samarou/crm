package com.itechart.security.business.dao;

import com.itechart.common.dao.StaticDataDao;
import com.itechart.security.business.model.persistent.Country;

public interface CountryDao extends StaticDataDao<Country> {

    Country getByName(String name);

}
