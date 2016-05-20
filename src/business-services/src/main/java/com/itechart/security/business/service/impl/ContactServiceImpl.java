package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.AddressDao;
import com.itechart.security.business.dao.ContactDao;
import com.itechart.security.business.dao.EmailDao;
import com.itechart.security.business.dao.SocialNetworkAccountDao;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;
import com.itechart.security.business.model.persistent.Address;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.model.persistent.Email;
import com.itechart.security.business.model.persistent.SocialNetworkAccount;
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
    private SocialNetworkAccountDao socialNetworkAccountDao;

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
    public void deleteSocialNetworkAccount(Long id) {
        socialNetworkAccountDao.delete(id);
    }

    @Override
    @Transactional
    public int countContacts(ContactFilter filter) {
        return contactDao.countContacts(filter);
    }
}
