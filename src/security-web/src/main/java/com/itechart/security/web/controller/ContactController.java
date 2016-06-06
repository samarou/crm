package com.itechart.security.web.controller;

import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;
import com.itechart.security.business.model.dto.ContactFilterDto;
import com.itechart.security.business.model.dto.DictionaryDto;
import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.service.ContactService;
import com.itechart.security.business.service.DictionaryService;
import com.itechart.security.business.service.ParsingService;
import com.itechart.security.model.dto.AclEntryDto;
import com.itechart.security.model.dto.DataPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.itechart.security.business.model.dto.utils.ContactConverter.convert;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author yauheni.putsykovich
 */
@RestController
@PreAuthorize("hasAnyRole('MANAGER', 'SPECIALIST')")
public class ContactController extends SecuredController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    ParsingService parsingService;

    @RequestMapping("/contacts/{contactId}/actions/{value}")
    public boolean isAllowed(@PathVariable Long contactId, @PathVariable String value) {
        return super.isAllowed(contactId, value);
    }

    @RequestMapping(value = "/contacts/{contactId}", method = RequestMethod.GET)
    public ContactDto get(@PathVariable Long contactId) {
        return contactService.get(contactId);
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

    @RequestMapping(value = "/contacts/{contactId}", method = RequestMethod.DELETE)
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

    @RequestMapping(value = "/contacts/{contactId}/acls/{principalId}", method = RequestMethod.DELETE)
    public void deleteAcl(@PathVariable Long contactId, @PathVariable Long principalId) {
        super.deleteAcl(contactId, principalId);
    }

    @RequestMapping(value = "/contacts/{contactId}/emails/{emailId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteEmail(@PathVariable Long contactId, @PathVariable Long emailId) {
        contactService.deleteEmail(emailId);
    }

    @RequestMapping(value = "/contacts/{contactId}/addresses/{addressId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteAddress(@PathVariable Long contactId, @PathVariable Long addressId) {
        contactService.deleteAddress(addressId);
    }

    @RequestMapping(value = "/contacts/{contactId}/messengers/{messengerId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteMessengerAccount(@PathVariable Long contactId, @PathVariable Long messengerId) {
        contactService.deleteMessengerAccount(messengerId);
    }

    @RequestMapping(value = "/contacts/{contactId}/social_networks/{socialNetworkId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteSocialNetworkAccount(@PathVariable Long contactId, @PathVariable Long socialNetworkId) {
        contactService.deleteSocialNetworkAccount(socialNetworkId);
    }

    @RequestMapping(value = "/contacts/{contactId}/telephones/{telephoneId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteTelephone(@PathVariable Long contactId, @PathVariable Long telephoneId) {
        contactService.deleteTelephone(telephoneId);
    }

    @RequestMapping(value = "/contacts/{contactId}/workplaces/{workplaceId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteWorkplace(@PathVariable Long contactId, @PathVariable Long workplaceId) {
        contactService.deleteWorkplace(workplaceId);
    }

    @RequestMapping(value = "/contacts/{contactId}/skills/{skillId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void deleteSkill(@PathVariable Long contactId, @PathVariable Long skillId) {
        contactService.deleteSkill(skillId);
    }

    @RequestMapping(value = "/dictionary", method = RequestMethod.GET)
    public DictionaryDto getDictionary(){return  dictionaryService.getDictionary();}

    @RequestMapping(value = "/parser/linkedIn", method = RequestMethod.GET)
    public ContactDto getFromSocialNetworkAccount(@RequestParam String url) throws IOException {
        return parsingService.parse(url);
    }

    @Override
    public ObjectTypes getObjectType() {
        return ObjectTypes.CONTACT;
    }
}
