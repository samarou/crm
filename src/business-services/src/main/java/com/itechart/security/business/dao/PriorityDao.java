package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.model.persistent.task.Priority;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public interface PriorityDao extends BaseDao<Priority, Long, PagingFilter> {
}
