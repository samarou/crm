package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.DynamicDataDaoImpl;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.TelephoneDao;
import com.itechart.security.business.model.persistent.Telephone;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class TelephoneDaoImpl extends DynamicDataDaoImpl<Telephone, Long, PagingFilter> implements TelephoneDao{

    @Override
    public void delete(Long id) {
        Telephone telephone = getHibernateTemplate().get(Telephone.class, id);
        if (telephone != null) {
            telephone.setDateDeleted(new Date());
            getHibernateTemplate().update(telephone);
        }
    }
}
