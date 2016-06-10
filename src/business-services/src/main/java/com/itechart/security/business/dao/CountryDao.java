package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.model.persistent.Country;

public interface CountryDao extends BaseDao<Country, Long, PagingFilter> {

    Country getByName(String name);

}
