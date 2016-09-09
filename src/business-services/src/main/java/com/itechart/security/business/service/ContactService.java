package com.itechart.security.business.service;



import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;
import com.itechart.security.business.model.dto.NationalityDto;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface ContactService {

    List<ContactDto> findContacts(ContactFilter filter);

    Long saveContact(ContactDto contact);

    ContactDto get(Long id);

    List <NationalityDto> getNationalities();

    ContactDto getByEmail(String email);

    void updateContact(ContactDto contact);

    void deleteById(Long id);

    void deleteEmail(Long id);

    void deleteAddress(Long id);

    void deleteMessengerAccount(Long id);

    void deleteSocialNetworkAccount(Long id);

    void deleteTelephone(Long id);

    void deleteWorkplace(Long id);

    void deleteAttachment(Long id);

    void deleteSkill(Long id);

    int countContacts(ContactFilter filter);

    void deleteUniversityEducation(Long id);
}
