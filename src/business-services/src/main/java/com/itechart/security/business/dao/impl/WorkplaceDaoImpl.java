package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.WorkplaceDao;
import com.itechart.security.business.model.persistent.Workplace;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class WorkplaceDaoImpl extends BaseHibernateDao<Workplace, Long, PagingFilter> implements WorkplaceDao {

    @Override
    public void delete(Long id) {
        Workplace workplace = getHibernateTemplate().get(Workplace.class, id);
        if (workplace != null) {
            workplace.setDateDeleted(new Date());
            getHibernateTemplate().update(workplace);
        }
    }
}
