package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.DynamicDataDaoImpl;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.MessengerAccountDao;
import com.itechart.security.business.model.persistent.MessengerAccount;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class MessengerAccountDaoImpl
        extends DynamicDataDaoImpl<MessengerAccount, Long, PagingFilter> implements MessengerAccountDao {

    @Override
    public void delete(Long id) {
        MessengerAccount account = getHibernateTemplate().get(MessengerAccount.class, id);
        if (account != null) {
            account.setDateDeleted(new Date());
            getHibernateTemplate().update(account);
        }
    }
}
