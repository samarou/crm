package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.NationalityDao;
import com.itechart.security.business.model.persistent.Nationality;
import org.springframework.stereotype.Repository;

@Repository
public class NationalityDaoImpl
        extends BaseHibernateDao<Nationality, Long, PagingFilter> implements NationalityDao {
}
