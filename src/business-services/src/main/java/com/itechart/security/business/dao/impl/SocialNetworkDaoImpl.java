package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.SocialNetworkDao;
import com.itechart.security.business.model.persistent.SocialNetwork;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SocialNetworkDaoImpl extends AbstractHibernateDao<SocialNetwork> implements SocialNetworkDao {

    @Override
    public List<SocialNetwork> loadAll() {
        return getHibernateTemplate().loadAll(SocialNetwork.class);
    }
}
