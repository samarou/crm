package com.itechart.security.web.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;
import com.itechart.security.business.model.dto.ContactFilterDto;
import com.itechart.security.business.model.dto.DictionaryDto;
import com.itechart.security.business.model.dto.NationalityDto;
import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.service.*;
import com.itechart.security.model.dto.AclEntryDto;
import com.itechart.security.web.model.dto.DataPageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.itechart.security.business.model.dto.utils.ContactConverter.convert;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author yauheni.putsykovich
 */
@RestController
@PreAuthorize("hasAnyRole('MANAGER', 'SPECIALIST')")
public class ContactController extends SecuredController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ParsingService parsingService;

    @Autowired
    private HistoryEntryService historyEntryService;

    @RequestMapping("/contacts/{contactId}/actions/{value}")
    public boolean isAllowed(@PathVariable Long contactId, @PathVariable String value) {
        return super.isAllowed(contactId, value);
    }

    @RequestMapping(value = "/contacts/{contactId}", method = GET)
    public ContactDto get(@PathVariable Long contactId) {
        return contactService.get(contactId);
    }

    @RequestMapping(value = "/contacts/nationalities", method = GET)
    public List<NationalityDto> getNationalities (){
        logger.debug("GETTING NATIONALITIES");
        return contactService.getNationalities();
    }

    @RequestMapping(value = "/contacts/email/{email}", method = GET)
    public String getContactByEmail(@PathVariable String email) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        ContactDto contact = contactService.getByEmail(email);
        return mapper.writeValueAsString(contact);
    }

    @PreAuthorize("hasPermission(#dto.getId(), 'sample.Contact', 'WRITE')")
    @RequestMapping(value = "/contacts", method = PUT)
    public void update(@RequestBody ContactDto dto) {
        contactService.updateContact(dto);
    }

    @RequestMapping("/contacts")
    public DataPageDto find(ContactFilterDto filterDto) {
        ContactFilter filter = convert(filterDto);
        DataPageDto<ContactDto> page = new DataPageDto<>();
        List<ContactDto> contactDtos = contactService.findContacts(filter);
        page.setData(contactDtos);
        page.setTotalCount(contactService.countContacts(filter));
        return page;
    }

    @RequestMapping(value = "/contacts", method = POST)
    public Long create(@RequestBody ContactDto dto) {
        Long contactId = contactService.saveContact(dto);
        super.createAcl(contactId);
        return contactId;
    }

    @RequestMapping(value = "/contacts/{contactId}", method = DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void delete(@PathVariable Long contactId) {
        super.deleteAcl(contactId);
        contactService.deleteById(contactId);
    }

    @RequestMapping("/contacts/{contactId}/acls")
    public List<AclEntryDto> getAcls(@PathVariable Long contactId) {
        return super.getAcls(contactId);
    }

    @RequestMapping(value = "/contacts/{contactId}/acls", method = PUT)
    public void createOrUpdateAcls(@PathVariable Long contactId, @RequestBody List<AclEntryDto> aclEntries) {
        super.createOrUpdateAcls(contactId, aclEntries);
    }

    @RequestMapping(value = "/contacts/{contactId}/acls/{principalId}", method = DELETE)
    public void deleteAcl(@PathVariable Long contactId, @PathVariable Long principalId) {
        super.deleteAcl(contactId, principalId);
    }

    @RequestMapping(value = "/contacts/{contactId}/emails/{emailId}", method = DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteEmail(@PathVariable Long contactId, @PathVariable Long emailId) {
        contactService.deleteEmail(emailId);
    }

    @RequestMapping(value = "/contacts/{contactId}/addresses/{addressId}", method = DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteAddress(@PathVariable Long contactId, @PathVariable Long addressId) {
        contactService.deleteAddress(addressId);
    }

    @RequestMapping(value = "/contacts/{contactId}/messengers/{messengerId}", method = DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteMessengerAccount(@PathVariable Long contactId, @PathVariable Long messengerId) {
        contactService.deleteMessengerAccount(messengerId);
    }

    @RequestMapping(value = "/contacts/{contactId}/social_networks/{socialNetworkId}", method = DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteSocialNetworkAccount(@PathVariable Long contactId, @PathVariable Long socialNetworkId) {
        contactService.deleteSocialNetworkAccount(socialNetworkId);
    }

    @RequestMapping(value = "/contacts/{contactId}/telephones/{telephoneId}", method = DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteTelephone(@PathVariable Long contactId, @PathVariable Long telephoneId) {
        contactService.deleteTelephone(telephoneId);
    }

    @RequestMapping(value = "/contacts/{contactId}/workplaces/{workplaceId}", method = DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteWorkplace(@PathVariable Long contactId, @PathVariable Long workplaceId) {
        contactService.deleteWorkplace(workplaceId);
    }

    @RequestMapping(value = "/contacts/{contactId}/attachments/{attachmentId}", method = DELETE)
    public void deleteAttachment(@PathVariable Long contactId, @PathVariable Long attachmentId) {
        logger.debug("deleting attachment for contact {}, attachment id {}", contactId, attachmentId);
        contactService.deleteAttachment(attachmentId);
    }

    @RequestMapping(value = "/contacts/{contactId}/skills/{skillId}", method = DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteSkill(@PathVariable Long contactId, @PathVariable Long skillId) {
        contactService.deleteSkill(skillId);
    }

    @RequestMapping(value = "/dictionary", method = GET)
    public DictionaryDto getDictionary(){return  dictionaryService.getDictionary();}

    @RequestMapping(value = "/parser/linkedIn", method = GET)
    public ContactDto getFromSocialNetworkAccount(@RequestParam String url) throws IOException {
        return parsingService.parse(url);
    }

    @Override
    public ObjectTypes getObjectType() {
        return ObjectTypes.CONTACT;
    }

    @RequestMapping(value = "/contacts/files/{contactId}/attachments/{attachmentId}/check", method = GET)
    public void checkFile(@PathVariable Long contactId, @PathVariable Long attachmentId) {
        logger.debug("checking attachment {} from contact {}", attachmentId, contactId);
        File file = fileService.getAttachment(contactId, attachmentId);
        if (!file.exists()) {
            throw new RuntimeException("error happened during file download");
        }
    }

    @RequestMapping(value = "/contacts/{contactId}/educations/{educationId}", method = DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteEducationInfo(@PathVariable Long contactId, @PathVariable Long educationId){
        logger.debug("deleting info about universities education for contact {}, education id {}",
                contactId, educationId);
        contactService.deleteUniversityEducation(educationId);
    }
}
