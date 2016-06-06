package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.DynamicDataDaoImpl;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.EmailDao;
import com.itechart.security.business.model.persistent.Email;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class EmailDaoImpl extends DynamicDataDaoImpl<Email, Long, PagingFilter> implements EmailDao {

    @Override
    public void delete(Long id) {
        Email email = getHibernateTemplate().get(Email.class, id);
        if (email != null) {
            email.setDateDeleted(new Date());
            getHibernateTemplate().update(email);
        }
    }
}
