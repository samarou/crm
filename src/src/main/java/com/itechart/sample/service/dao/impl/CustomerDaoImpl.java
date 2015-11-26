package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.business.Customer;
import com.itechart.sample.security.annotation.AclObjectFilter;
import com.itechart.sample.service.dao.CustomerDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class CustomerDaoImpl extends BaseHibernateDao<Customer> implements CustomerDao {

    @Override
    @AclObjectFilter
    public List<Customer> loadAll() {
        return new ArrayList<>();//super.loadAll();
    }
}