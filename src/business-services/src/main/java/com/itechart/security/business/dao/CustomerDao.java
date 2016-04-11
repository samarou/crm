package com.itechart.security.business.dao;

import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.persistent.Contact;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface CustomerDao {

    Long save(Contact contact);

    List<Contact> loadAll();

    void update(Contact contact);

    void deleteById(Long id);

    List findCustomers(ContactFilter filter);

    int countCustomers(ContactFilter filter);
}
