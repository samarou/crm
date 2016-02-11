package com.itechart.security.business.dao.impl;

import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.business.dao.CustomerDao;
import com.itechart.security.business.model.persistent.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        return new ArrayList<>();//return getHibernateTemplate().loadAll(Customer.class);
    }
}