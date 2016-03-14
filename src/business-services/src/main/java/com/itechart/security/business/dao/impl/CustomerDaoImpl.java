package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.CustomerDao;
import com.itechart.security.business.filter.CustomerFilter;
import com.itechart.security.business.model.persistent.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class CustomerDaoImpl extends AbstractHibernateDao implements CustomerDao {

    @Override
    public Long save(Customer customer) {
        return (Long) getHibernateTemplate().save(customer);
    }

    @Override
    //@AclFilter
    //@AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.WRITE}, inherit = true))
//    @AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.WRITE}))
    public List<Customer> loadAll() {
        return getHibernateTemplate().loadAll(Customer.class);
    }

    @Override
    public void update(Customer customer) {
        getHibernateTemplate().update(customer);
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = getHibernateTemplate().get(Customer.class, id);
        if(customer != null) getHibernateTemplate().delete(customer);
    }

    @Override
    public List<Customer> findCustomers(CustomerFilter filter) {
        return loadAll();
    }
}