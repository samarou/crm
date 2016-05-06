package com.itechart.security.business.service;



import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;
import com.itechart.security.business.model.persistent.Contact;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface ContactService {

    List<ContactDto> findContacts(ContactFilter filter);

    Long saveContact(ContactDto contact);

    ContactDto get(Long id);

    List<ContactDto> getContacts();

    void updateContact(ContactDto contact);

    void deleteById(Long id);

    void deleteEmail(Long id);

    int countContacts(ContactFilter filter);
}
