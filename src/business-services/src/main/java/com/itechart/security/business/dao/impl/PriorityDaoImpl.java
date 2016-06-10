package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.StaticDataDaoImpl;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.PriorityDao;
import com.itechart.security.business.model.persistent.task.Priority;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class PriorityDaoImpl extends StaticDataDaoImpl<Priority, Long, PagingFilter> implements PriorityDao {

}
