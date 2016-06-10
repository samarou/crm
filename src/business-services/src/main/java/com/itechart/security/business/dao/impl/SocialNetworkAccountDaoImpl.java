package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.SocialNetworkAccountDao;
import com.itechart.security.business.model.persistent.SocialNetworkAccount;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SocialNetworkAccountDaoImpl
        extends BaseHibernateDao<SocialNetworkAccount, Long, PagingFilter> implements SocialNetworkAccountDao {

    @Override
    public void delete(Long id) {
        SocialNetworkAccount account = getHibernateTemplate().get(SocialNetworkAccount.class, id);
        if (account != null) {
            account.setDateDeleted(new Date());
            getHibernateTemplate().update(account);
        }
    }
}
