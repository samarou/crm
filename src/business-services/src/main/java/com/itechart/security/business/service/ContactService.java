package com.itechart.security.business.service;



import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;

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

    void deleteAddress(Long id);

    void deleteSocialNetworkAccount(Long id);

    void deleteTelephone(Long id);

    int countContacts(ContactFilter filter);
}
