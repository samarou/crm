package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.common.dao.impl.DynamicDataDaoImpl;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.AddressDao;
import com.itechart.security.business.model.persistent.Address;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class AddressDaoImpl extends DynamicDataDaoImpl<Address, Long, PagingFilter> implements AddressDao {

    @Override
    public void delete(Long id) {
        Address address = getHibernateTemplate().get(Address.class, id);
        if (address != null) {
            address.setDateDeleted(new Date());
            update(address);
        }
    }
}
