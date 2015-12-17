package com.itechart.sample.service.impl;

import com.itechart.sample.model.persistent.business.Customer;
import com.itechart.sample.service.CustomerService;
import com.itechart.sample.service.dao.CustomerDao;
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
}
