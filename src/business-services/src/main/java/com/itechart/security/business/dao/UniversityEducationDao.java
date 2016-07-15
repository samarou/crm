package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.model.persistent.UniversityEducation;
import org.springframework.stereotype.Repository;


public interface UniversityEducationDao extends BaseDao<UniversityEducation,Long,PagingFilter> {
}
