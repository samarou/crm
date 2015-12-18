package com.itechart.security.sample.dao;

import com.itechart.security.sample.model.persistent.Customer;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface CustomerDao {

    List<Customer> loadAll();
}
