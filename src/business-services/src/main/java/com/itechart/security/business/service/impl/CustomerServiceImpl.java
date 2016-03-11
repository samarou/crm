package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.CustomerDao;
import com.itechart.security.business.filter.CustomerFilter;
import com.itechart.security.business.model.persistent.Customer;
import com.itechart.security.business.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author andrei.samarou
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    @Transactional
    public Long saveCustomer(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    //@PreAuthorize("hasPrivilege('sample.Customer', 'READ') or hasRole('ROOT')")
    //@PreAuthorize("@mySecurityService.hasPermission('special')")
    //@PreAuthorize("hasPermission(#objectId, 'ObjectType', 'READ')")
    //@PreFilter("filterObject.property == authentication.name")
    //@PostFilter ("filterObject.owner == authentication.name")
    //@PostFilter("hasPermission(filterObject, 'READ')")
    @PreAuthorize("hasRole('USER')")
    public List<Customer> getCustomers() {
        return customerDao.loadAll();
    }

    @Override
    @Transactional
    public void updateCustomer(Customer customer) {
        customerDao.update(customer);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        customerDao.deleteById(id);
    }

    @Override
    public List<Customer> findCustomers(CustomerFilter filter) {
        return customerDao.findCustomers(filter);
    }

    @Override
    public int countCustomers(CustomerFilter filter) {
        return customerDao.countCustomers(filter);
    }
}
