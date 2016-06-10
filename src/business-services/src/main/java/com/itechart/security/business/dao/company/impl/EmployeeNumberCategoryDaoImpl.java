package com.itechart.security.business.dao.company.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.company.EmployeeNumberCategoryDao;
import com.itechart.security.business.model.persistent.company.EmployeeNumberCategory;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeNumberCategoryDaoImpl
        extends BaseHibernateDao<EmployeeNumberCategory, Long, PagingFilter> implements EmployeeNumberCategoryDao {
}
