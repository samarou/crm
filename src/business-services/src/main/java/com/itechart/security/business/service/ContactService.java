package com.itechart.security.business.service;



import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.persistent.Contact;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface ContactService {

    List<Contact> findContacts(ContactFilter filter);

    Long saveContact(Contact contact);

    Contact get(Long id);

    List<Contact> getContacts();

    void updateContact(Contact contact);

    void deleteById(Long id);

    int countContacts(ContactFilter filter);
}
