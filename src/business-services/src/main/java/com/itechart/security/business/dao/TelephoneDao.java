package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.model.persistent.Telephone;

public interface TelephoneDao extends BaseDao<Telephone, Long, PagingFilter> {
}
