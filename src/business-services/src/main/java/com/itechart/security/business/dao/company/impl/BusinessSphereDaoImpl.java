package com.itechart.security.business.dao.company.impl;

import com.itechart.common.dao.impl.StaticDataDaoImpl;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.company.BusinessSphereDao;
import com.itechart.security.business.model.persistent.company.BusinessSphere;
import org.springframework.stereotype.Repository;

@Repository
public class BusinessSphereDaoImpl
        extends StaticDataDaoImpl<BusinessSphere, Long, PagingFilter> implements BusinessSphereDao {
}
