package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.StatusDao;
import com.itechart.security.business.model.persistent.task.Status;
import org.springframework.stereotype.Repository;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class StatusDaoImpl extends BaseHibernateDao<Status, Long, PagingFilter> implements StatusDao {
}
