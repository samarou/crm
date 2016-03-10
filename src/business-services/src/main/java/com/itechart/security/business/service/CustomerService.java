package com.itechart.security.business.service;

import com.itechart.security.business.model.persistent.Customer;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface CustomerService {

    List<Customer> getCustomers();

    void update(Customer customer);
}
