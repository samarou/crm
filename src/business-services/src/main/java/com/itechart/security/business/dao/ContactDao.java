package com.itechart.security.business.dao;

import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.persistent.Contact;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface ContactDao {

    Long save(Contact contact);

    List<Contact> loadAll();

    void update(Contact contact);

    void deleteById(Long id);

    List findContacts(ContactFilter filter);

    int countContacts(ContactFilter filter);
}