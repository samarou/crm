package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.ContactDao;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;
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

    @Override
    @Transactional
    public List<ContactDto> findContacts(ContactFilter filter) {
        return convertContacts(contactDao.findContacts(filter));
    }

    @Override
    @Transactional
    public Long saveContact(ContactDto contactDto) {
        return contactDao.save(convert(contactDto));
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
        contactDao.update(convert(contactDto));
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
