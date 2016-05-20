package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.MessengerAccountDao;
import com.itechart.security.business.model.persistent.MessengerAccount;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class MessengerAccountDaoImpl extends AbstractHibernateDao<MessengerAccount> implements MessengerAccountDao {
    @Override
    public Long save(MessengerAccount account) {
        return (Long) getHibernateTemplate().save(account);
    }

    @Override
    public void update(MessengerAccount account) {
        getHibernateTemplate().update(account);
    }

    @Override
    public void delete(Long id) {
        MessengerAccount account = getHibernateTemplate().get(MessengerAccount.class, id);
        if (account != null) {
            account.setDateDeleted(new Date());
            getHibernateTemplate().update(account);
        }
    }
}
