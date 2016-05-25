package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.SocialNetworkAccountDao;
import com.itechart.security.business.model.persistent.SocialNetworkAccount;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SocialNetworkAccountDaoImpl extends AbstractHibernateDao<SocialNetworkAccount>  implements SocialNetworkAccountDao {
    @Override
    public Long save(SocialNetworkAccount account) {
        return (Long) getHibernateTemplate().save(account);
    }

    @Override
    public void update(SocialNetworkAccount account) {
        getHibernateTemplate().update(account);
    }

    @Override
    public void delete(Long id) {
        SocialNetworkAccount account = getHibernateTemplate().get(SocialNetworkAccount.class, id);
        if (account != null) {
            account.setDateDeleted(new Date());
            getHibernateTemplate().update(account);
        }
    }
}
