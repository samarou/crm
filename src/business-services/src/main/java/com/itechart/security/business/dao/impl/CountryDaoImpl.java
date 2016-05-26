package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.CountryDao;
import com.itechart.security.business.model.persistent.Country;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryDaoImpl extends AbstractHibernateDao<Country> implements CountryDao {
    @Override
    public List<Country> loadAll() {
        return getHibernateTemplate().loadAll(Country.class);
    }

    @Override
    public Country get(Long id) {
        return getHibernateTemplate().get(Country.class, id);
    }

    @Override
    public Country getByName(String name) {
        return (Country) getHibernateTemplate()
                .findByNamedParam("from Country where name=:name", "name", name)
                .stream().findFirst().orElse(null);
    }
}
