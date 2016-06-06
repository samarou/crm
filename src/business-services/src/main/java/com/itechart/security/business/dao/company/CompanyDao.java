package com.itechart.security.business.dao.company;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.dao.DynamicDataDao;
import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.model.persistent.company.Company;

public interface CompanyDao extends BaseDao<Company, Long, CompanyFilter>, DynamicDataDao<Company, Long> {

}
