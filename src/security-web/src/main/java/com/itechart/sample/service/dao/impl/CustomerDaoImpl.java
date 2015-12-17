package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.business.Customer;
import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.sample.service.dao.CustomerDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class CustomerDaoImpl extends BaseHibernateDao<Customer> implements CustomerDao {

    @Override
    //@AclFilter
    @AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.WRITE}))
    //@AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.WRITE}, inherit = true))
    public List<Customer> loadAll() {
        return super.loadAll();
    }
}