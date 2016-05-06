package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.EmailDao;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.model.persistent.Email;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class EmailDaoImpl extends AbstractHibernateDao<Contact> implements EmailDao {
    @Override
    public Long save(Email email) {
        return (Long) getHibernateTemplate().save(email);
    }

    @Override
    public void update(Email email) {
        getHibernateTemplate().update(email);
    }

    @Override
    public void delete(Long id) {
        Email email = getHibernateTemplate().get(Email.class, id);
        if (email != null) {
            email.setDateDeleted(new Date());
            getHibernateTemplate().update(email);
        }
    }
}
