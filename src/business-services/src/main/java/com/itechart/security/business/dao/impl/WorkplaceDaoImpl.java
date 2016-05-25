package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.WorkplaceDao;
import com.itechart.security.business.model.persistent.Workplace;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class WorkplaceDaoImpl extends AbstractHibernateDao<Workplace> implements WorkplaceDao {
    @Override
    public Long save(Workplace workplace) {
        return (Long) getHibernateTemplate().save(workplace);
    }

    @Override
    public void update(Workplace workplace) {
        getHibernateTemplate().update(workplace);
    }

    @Override
    public void delete(Long id) {
        Workplace workplace = getHibernateTemplate().get(Workplace.class, id);
        if (workplace != null) {
            workplace.setDateDeleted(new Date());
            getHibernateTemplate().update(workplace);
        }
    }
}
