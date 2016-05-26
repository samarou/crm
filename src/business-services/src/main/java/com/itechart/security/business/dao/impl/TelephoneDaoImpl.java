package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.TelephoneDao;
import com.itechart.security.business.model.persistent.Telephone;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class TelephoneDaoImpl extends AbstractHibernateDao<Telephone> implements TelephoneDao{
    @Override
    public Long save(Telephone telephone) {
        return (Long) getHibernateTemplate().save(telephone);
    }

    @Override
    public void update(Telephone telephone) {
        getHibernateTemplate().update(telephone);
    }

    @Override
    public void delete(Long id) {
        Telephone telephone = getHibernateTemplate().get(Telephone.class, id);
        if (telephone != null) {
            telephone.setDateDeleted(new Date());
            getHibernateTemplate().update(telephone);
        }
    }
}
