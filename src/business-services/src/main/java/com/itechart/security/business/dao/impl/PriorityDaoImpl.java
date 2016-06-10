package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.PriorityDao;
import com.itechart.security.business.model.persistent.task.Priority;
import org.springframework.stereotype.Repository;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class PriorityDaoImpl extends BaseHibernateDao<Priority, Long, PagingFilter> implements PriorityDao {

}
