package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.business.Customer;
import com.itechart.sample.service.dao.CustomerDao;
import org.springframework.stereotype.Repository;

/**
 * @author andrei.samarou
 */
@Repository
public class CustomerDaoImpl extends BaseHibernateDao<Customer> implements CustomerDao {
}
