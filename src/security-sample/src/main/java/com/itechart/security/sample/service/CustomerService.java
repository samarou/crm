package com.itechart.security.sample.service;

import com.itechart.security.sample.model.persistent.Customer;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface CustomerService {

    List<Customer> getCustomers();
}
