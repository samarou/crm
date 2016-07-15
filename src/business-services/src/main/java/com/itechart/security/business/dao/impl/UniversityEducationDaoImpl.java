package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.UniversityEducationDao;
import com.itechart.security.business.model.persistent.UniversityEducation;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UniversityEducationDaoImpl extends BaseHibernateDao<UniversityEducation, Long, PagingFilter>
        implements UniversityEducationDao {

    @Override
    public void delete(Long id){
        UniversityEducation education = getHibernateTemplate().get(UniversityEducation.class, id);
        if(education != null){
            education.setDateDeleted(new Date());
            getHibernateTemplate().update(education);
        }
    }
}
