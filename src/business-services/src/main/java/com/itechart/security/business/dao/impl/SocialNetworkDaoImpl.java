package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.SocialNetworkDao;
import com.itechart.security.business.model.persistent.SocialNetwork;
import org.springframework.stereotype.Repository;

@Repository
public class SocialNetworkDaoImpl
        extends BaseHibernateDao<SocialNetwork, Long, PagingFilter> implements SocialNetworkDao {
}
