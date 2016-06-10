package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.CountryDao;
import com.itechart.security.business.model.persistent.Country;
import org.springframework.stereotype.Repository;

@Repository
public class CountryDaoImpl extends BaseHibernateDao<Country, Long, PagingFilter> implements CountryDao {

    @Override
    public Country getByName(String name) {
        return findOne("from Country where name=?", name);
    }
}
