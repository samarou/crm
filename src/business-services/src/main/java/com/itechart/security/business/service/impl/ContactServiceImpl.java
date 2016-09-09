package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.*;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;
import com.itechart.security.business.model.dto.HistoryEntryDto;
import com.itechart.security.business.model.dto.NationalityDto;
import com.itechart.security.business.model.dto.utils.CompanyConverter;
import com.itechart.security.business.model.dto.utils.ContactConverter;
import com.itechart.security.business.model.dto.utils.DtoConverter;
import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.model.persistent.*;
import com.itechart.security.business.model.persistent.ObjectKey;
import com.itechart.security.business.service.ContactService;
import com.itechart.security.business.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itechart.security.business.service.HistoryEntryService;
import com.itechart.security.model.persistent.ObjectType;
import com.itechart.security.service.ObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.util.List;

import static com.itechart.common.model.util.CollectionConverter.convertCollection;
import static com.itechart.security.business.model.dto.utils.DtoConverter.convert;
import static com.itechart.security.business.model.dto.utils.DtoConverter.convertContacts;

/**
 * @author andrei.samarou
 */
@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

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

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    private UniversityEducationDao universityEducationDao;

    @Autowired
    private NationalityDao nationalityDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private HistoryEntryService historyEntryService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @Override
    @Transactional (readOnly = true)
    public List<NationalityDto> getNationalities(){
        List<NationalityDto> nationalities = DtoConverter.convertNationalities(nationalityDao.loadAll());
        logger.debug("GET ALL NATIONALITIES" + nationalities.toString());
        return nationalities;
    }

    @Override
    @Transactional
    public List<ContactDto> findContacts(ContactFilter filter) {
        List<Contact> contacts = contactDao.find(filter);
        return convertContacts(contacts);
    }

    @Override
    @Transactional
    public Long saveContact(ContactDto contactDto) {
        Contact contact = convert(contactDto);
        moveAvatarImageToTargetDirectory(contact);
        Long contactId = contactDao.save(contact);
        historyEntryService.startHistory(buildObjectKey(contactId));
        moveFilesToTargetDirectory(contact);
        return contactId;
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDto get(Long id) {
        ContactDto contactDto = convert(contactDao.get(id));
        HistoryEntryDto historyEntry = historyEntryService.getLastModification(buildObjectKey(id));
        contactDto.setHistory(historyEntry);
        return contactDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDto getByEmail(String email){
        Contact contact = contactDao.getByEmail(email);
        return contact != null? convert(contact):null;
    }

    private ObjectKey buildObjectKey(long contactId) {
        ObjectType objectType = objectTypeService.getObjectTypeByName(ObjectTypes.CONTACT.getName());
        return new ObjectKey(objectType.getId(), contactId);
    }

    @Override
    @Transactional
    public void updateContact(ContactDto contactDto) {
        Contact contact = convert(contactDto);
        moveAvatarImageToTargetDirectory(contact);
        contactDao.update(contact);
        moveFilesToTargetDirectory(contact);
        historyEntryService.updateHistory(getObjectIdentityId(contact.getId()));
    }

    private ObjectKey getObjectIdentityId(long contactId) {
        ObjectType objectType = objectTypeService.getObjectTypeByName(ObjectTypes.CONTACT.getName());
        return new ObjectKey(objectType.getId(), contactId);
    }

    private void moveFilesToTargetDirectory(Contact contact) {
        for (Attachment attachment : contact.getAttachments()) {
            if (attachment.getFilePath() != null) {
                try {
                    fileService.moveFileToContactDirectory(attachment.getFilePath(), contact.getId(), attachment.getId());
                } catch (IOException e) {
                    logger.error("can't save file to attachment directory for contactId: {}, attachmentId: {}, tempPath: {}", contact.getId(), attachment.getId(), attachment.getFilePath(), e);
                    throw new RuntimeException("error while saving attachment");
                }
            }
        }
    }

    private void moveAvatarImageToTargetDirectory(Contact contact){
        if(StringUtils.isNotBlank(contact.getPhotoUrl())
                && !isCorrectWebUrl(contact.getPhotoUrl())){
            try {
                fileService.moveImageToContactDirectory(contact);
            }catch(IOException e){
                logger.error("can't save image to attachment directory for contact: {}, tempPath: {}", contact.getId(), contact.getPhotoUrl());
            }
        }
    }

    private boolean isCorrectWebUrl(String url){
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        contactDao.delete(id);
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
    public void deleteAttachment(Long id) {
        attachmentDao.delete(id);
    }

    @Override
    @Transactional
    public void deleteSkill(Long id) {
        contactDao.deleteSkill(id);
    }

    @Override
    @Transactional
    public int countContacts(ContactFilter filter) {
        return contactDao.count(filter);
    }

    @Override
    @Transactional
    public void deleteUniversityEducation(Long id){
        universityEducationDao.delete(id);
    }
}
