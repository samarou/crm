package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.business.Customer;
import com.itechart.sample.model.security.Permission;
import com.itechart.sample.security.annotation.AclObjectFilter;
import com.itechart.sample.security.annotation.AclRule;
import com.itechart.sample.service.dao.CustomerDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class CustomerDaoImpl extends BaseHibernateDao<Customer> implements CustomerDao {

    @Override
    //@AclObjectFilter
    @AclObjectFilter(@AclRule(type = Customer.class, permissions = {Permission.WRITE}))
    //@AclObjectFilter(@AclRule(type = Customer.class, permissions = {Permission.WRITE}, inherit = true))
    public List<Customer> loadAll() {
        return super.loadAll();
    }
}