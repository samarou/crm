package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.model.persistent.Nationality;

public interface NationalityDao extends BaseDao<Nationality, Long, PagingFilter> {
}
