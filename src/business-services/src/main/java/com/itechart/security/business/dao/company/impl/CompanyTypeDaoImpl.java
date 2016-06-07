package com.itechart.security.business.dao.company.impl;

import com.itechart.common.dao.impl.StaticDataDaoImpl;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.company.CompanyTypeDao;
import com.itechart.security.business.model.persistent.company.CompanyType;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyTypeDaoImpl
        extends StaticDataDaoImpl<CompanyType, Long, PagingFilter> implements CompanyTypeDao {
}
