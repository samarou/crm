package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.MessengerDao;
import com.itechart.security.business.model.persistent.Messenger;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessengerDaoImpl extends AbstractHibernateDao<Messenger> implements MessengerDao{
    @Override
    public List<Messenger> loadAll() {
        return getHibernateTemplate().loadAll(Messenger.class);
    }
}
