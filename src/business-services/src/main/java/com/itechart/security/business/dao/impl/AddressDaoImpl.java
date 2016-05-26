package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.AddressDao;
import com.itechart.security.business.model.persistent.Address;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class AddressDaoImpl extends AbstractHibernateDao<Address> implements AddressDao {
    @Override
    public Long save(Address address) {
        return (Long) getHibernateTemplate().save(address);
    }

    @Override
    public void update(Address address) {
        getHibernateTemplate().update(address);
    }

    @Override
    public void delete(Long id) {
        Address address = getHibernateTemplate().get(Address.class, id);
        if (address != null) {
            address.setDateDeleted(new Date());
            getHibernateTemplate().update(address);
        }
    }
}
