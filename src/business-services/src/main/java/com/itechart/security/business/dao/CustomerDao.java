package com.itechart.security.business.dao;

import com.itechart.security.business.filter.CustomerFilter;
import com.itechart.security.business.model.persistent.Customer;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface CustomerDao {

    Long save(Customer customer);

    List<Customer> loadAll();

    void update(Customer customer);

    void deleteById(Long id);

    List<Customer> findCustomers(CustomerFilter filter);

    int countCustomers(CustomerFilter filter);
}
