package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.ContactDao;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author andrei.samarou
 */
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao contactDao;

    @Override
    @Transactional
    public List<Contact> findContacts(ContactFilter filter) {
        return contactDao.findContacts(filter);
    }

    @Override
    @Transactional
    public Long saveContact(Contact contact) {
        return contactDao.save(contact);
    }

    @Override
    @Transactional(readOnly = true)
    //@PreAuthorize("hasPrivilege('sample.Contact', 'READ') or hasRole('ROOT')")
    //@PreAuthorize("@mySecurityService.hasPermission('special')")
    //@PreAuthorize("hasPermission(#objectId, 'ObjectType', 'READ')")
    //@PreFilter("filterObject.property == authentication.name")
    //@PostFilter ("filterObject.owner == authentication.name")
    //@PostFilter("hasPermission(filterObject, 'READ')")
    public List<Contact> getContacts() {
        return contactDao.loadAll();
    }

    @Override
    @Transactional
    public void updateContact(Contact contact) {
        contactDao.update(contact);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        contactDao.deleteById(id);
    }

    @Override
    @Transactional
    public int countContacts(ContactFilter filter) {
        return contactDao.countContacts(filter);
    }
}
