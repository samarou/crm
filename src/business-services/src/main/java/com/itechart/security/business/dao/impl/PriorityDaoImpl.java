package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.PriorityDao;
import com.itechart.security.business.model.persistent.task.Priority;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class PriorityDaoImpl extends AbstractHibernateDao<Priority> implements PriorityDao {

    @Override
    public List<Priority> loadAll() {
        return getHibernateTemplate().loadAll(Priority.class);
    }

}
