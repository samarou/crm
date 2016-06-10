package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.AbstractHibernateDao;
import com.itechart.security.business.dao.StatusDao;
import com.itechart.security.business.model.persistent.task.Status;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class StatusDaoImpl extends AbstractHibernateDao<Status> implements StatusDao {

    @Override
    public List<Status> loadAll() {
        return getHibernateTemplate().loadAll(Status.class);
    }

}
