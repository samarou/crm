package com.itechart.security.sample.dao.impl;

import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.sample.dao.CustomerDao;
import com.itechart.security.sample.model.persistent.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class CustomerDaoImpl extends AbstractHibernateDao implements CustomerDao {

    @Override
    //@AclFilter
    //@AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.WRITE}, inherit = true))
    @AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.WRITE}))
    public List<Customer> loadAll() {
        return getHibernateTemplate().loadAll(Customer.class);
    }
}