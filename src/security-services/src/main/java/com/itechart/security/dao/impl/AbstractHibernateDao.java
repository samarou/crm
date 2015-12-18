package com.itechart.security.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

/**
 * @author andrei.samarou
 */
class AbstractHibernateDao extends HibernateDaoSupport {

    @Autowired
    @Qualifier("securitySessionFactory")
    public void init(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> find(String queryString, Object... values) {
        return (List<T>) getHibernateTemplate().find(queryString, values);
    }

    @SuppressWarnings("unchecked")
    protected <T> T findObject(String queryString, Object... values) {
        List<?> result = getHibernateTemplate().find(queryString, values);
        return !result.isEmpty() ? (T) result.get(0) : null;
    }
}