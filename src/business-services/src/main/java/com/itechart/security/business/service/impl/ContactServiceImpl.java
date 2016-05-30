package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.*;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;
import com.itechart.security.business.model.persistent.*;
import com.itechart.security.business.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itechart.security.business.model.dto.utils.DtoConverter.convert;
import static com.itechart.security.business.model.dto.utils.DtoConverter.convertContacts;

/**
 * @author andrei.samarou
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private MessengerAccountDao messengerAccountDao;

    @Autowired
    private SocialNetworkAccountDao socialNetworkAccountDao;

    @Autowired
    private TelephoneDao telephoneDao;

    @Autowired
    private WorkplaceDao workplaceDao;

    @Override
    @Transactional
    public List<ContactDto> findContacts(ContactFilter filter) {
        List<Contact> contacts = contactDao.findContacts(filter);
        return convertContacts(contacts);
    }

    @Override
    @Transactional
    public Long saveContact(ContactDto contactDto) {
        Contact contact = convert(contactDto);
        Long contactId = contactDao.save(contact);
        saveOrUpdateEmailsForContact(contact);
        saveOrUpdateAddressesForContact(contact);
        saveOrUpdateSocialNetworkAccountsForContact(contact);
        saveOrUpdateTelephonesForContact(contact);
        saveOrUpdateMessengersForContact(contact);
        saveOrUpdateWorkplacesForContact(contact);
        setContactForFields(contact);
        return contactId;
    }

    @Override
    @Transactional
    public ContactDto get(Long id) {
        return convert(contactDao.get(id));
    }

    @Override
    @Transactional(readOnly = true)
    //@PreAuthorize("hasPrivilege('sample.Contact', 'READ') or hasRole('ROOT')")
    //@PreAuthorize("@mySecurityService.hasPermission('special')")
    //@PreAuthorize("hasPermission(#objectId, 'ObjectType', 'READ')")
    //@PreFilter("filterObject.property == authentication.name")
    //@PostFilter ("filterObject.owner == authentication.name")
    //@PostFilter("hasPermission(filterObject, 'READ')")
    public List<ContactDto> getContacts() {
        return convertContacts(contactDao.loadAll());
    }

    @Override
    @Transactional
    public void updateContact(ContactDto contactDto) {
        Contact contact = convert(contactDto);
        saveOrUpdateEmailsForContact(contact);
        saveOrUpdateAddressesForContact(contact);
        saveOrUpdateSocialNetworkAccountsForContact(contact);
        saveOrUpdateTelephonesForContact(contact);
        saveOrUpdateMessengersForContact(contact);
        saveOrUpdateWorkplacesForContact(contact);
        setContactForFields(contact);
        contactDao.update(contact);
    }

    private void saveOrUpdateEmailsForContact(Contact contact){
        for (Email email : contact.getEmails()) {
            email.setContact(contact);
            if (email.getId() == null) {
                emailDao.save(email);
            } else {
                emailDao.update(email);
            }
        }
    }

    private void saveOrUpdateAddressesForContact(Contact contact){
        for (Address address : contact.getAddresses()) {
            address.setContact(contact);
            if (address.getId() == null) {
                addressDao.save(address);
            } else {
                addressDao.update(address);
            }
        }
    }

    private void saveOrUpdateSocialNetworkAccountsForContact(Contact contact){
        for (SocialNetworkAccount account : contact.getSocialNetworks()) {
            account.setContact(contact);
            if (account.getId() == null) {
                socialNetworkAccountDao.save(account);
            } else {
                socialNetworkAccountDao.update(account);
            }
        }
    }

    private void saveOrUpdateTelephonesForContact(Contact contact){
        for (Telephone telephone : contact.getTelephones()) {
            telephone.setContact(contact);
            if (telephone.getId() == null) {
                telephoneDao.save(telephone);
            } else {
                telephoneDao.update(telephone);
            }
        }
    }

    private void saveOrUpdateMessengersForContact(Contact contact){
        for (MessengerAccount messenger : contact.getMessengers()) {
            messenger.setContact(contact);
            if (messenger.getId() == null) {
                messengerAccountDao.save(messenger);
            } else {
                messengerAccountDao.update(messenger);
            }
        }
    }

    private void saveOrUpdateWorkplacesForContact(Contact contact){
        for (Workplace workplace : contact.getWorkplaces()) {
            workplace.setContact(contact);
            if (workplace.getId() == null) {
                workplaceDao.save(workplace);
            } else {
                workplaceDao.update(workplace);
            }
        }
    }

    private void setContactForFields(Contact contact) {
        for (Skill skill : contact.getSkills()) {
            skill.setContact(contact);
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        contactDao.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteEmail(Long id) {
        emailDao.delete(id);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        addressDao.delete(id);
    }

    @Override
    @Transactional
    public void deleteMessengerAccount(Long id) {
        messengerAccountDao.delete(id);
    }

    @Override
    @Transactional
    public void deleteSocialNetworkAccount(Long id) {
        socialNetworkAccountDao.delete(id);
    }

    @Override
    @Transactional
    public void deleteTelephone(Long id) {
        telephoneDao.delete(id);
    }

    @Override
    @Transactional
    public void deleteWorkplace(Long id) {
        workplaceDao.delete(id);
    }

    @Override
    @Transactional
    public void deleteSkill(Long id) {
        contactDao.deleteSkill(id);
    }

    @Override
    @Transactional
    public int countContacts(ContactFilter filter) {
        return contactDao.countContacts(filter);
    }
}
