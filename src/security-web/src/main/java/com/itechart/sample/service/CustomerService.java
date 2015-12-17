package com.itechart.sample.service;

import com.itechart.sample.model.persistent.business.Customer;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface CustomerService {

    List<Customer> getCustomers();
}
