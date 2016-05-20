package com.itechart.security.web.controller;

import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.*;
import com.itechart.security.business.model.enums.EmailType;
import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.model.enums.TelephoneType;
import com.itechart.security.business.service.ContactService;
import com.itechart.security.business.service.DictionaryService;
import com.itechart.security.core.SecurityUtils;
import com.itechart.security.core.acl.AclPermissionEvaluator;
import com.itechart.security.core.model.acl.ObjectIdentity;
import com.itechart.security.core.model.acl.ObjectIdentityImpl;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.service.AclService;
import com.itechart.security.service.PrincipalService;
import com.itechart.security.web.model.dto.AclEntryDto;
import com.itechart.security.web.model.dto.ContactFilterDto;
import com.itechart.security.web.model.dto.DataPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.itechart.security.business.model.dto.utils.DtoConverter.convertEmailTypes;
import static com.itechart.security.business.model.dto.utils.DtoConverter.convertTelephoneTypes;
import static com.itechart.security.web.model.dto.Converter.convert;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author yauheni.putsykovich
 */
@RestController
@PreAuthorize("hasAnyRole('MANAGER', 'SPECIALIST')")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private AclService aclService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private AclPermissionEvaluator aclPermissionEvaluator;

    @RequestMapping("/contacts/{contactId}/actions/{value}")
    public boolean isAllowed(@PathVariable Long contactId, @PathVariable String value) {
        Permission permission = Permission.valueOf(value.toUpperCase());
        return aclPermissionEvaluator.hasPermission(SecurityUtils.getAuthentication(), createIdentity(contactId), permission);
    }

    @RequestMapping("/contacts")
    public List<ContactDto> getContacts() {
        return contactService.getContacts();
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

    @RequestMapping("/contacts/find")
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
        Long userId = SecurityUtils.getAuthenticatedUserId();
        aclService.createAcl(new ObjectIdentityImpl(contactId, ObjectTypes.CONTACT.getName()), null, userId);
        return contactId;
    }

    @RequestMapping(value = "/contacts/{contactId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#contactId, 'sample.Contact', 'DELETE')")
    public void delete(@PathVariable Long contactId) {
        Acl acl = getAcl(contactId);
        aclService.deleteAcl(acl);
        contactService.deleteById(contactId);
    }

    @RequestMapping("/contacts/{contactId}/acls")
    public List<AclEntryDto> getAcls(@PathVariable Long contactId) {
        Acl acl = getAcl(contactId);
        Map<Long, Set<Permission>> allPermissions = acl.getPermissions();
        List<Principal> principals = principalService.getByIds(new ArrayList<>(allPermissions.keySet()));
        return principals.stream().map(principal -> convert(principal, allPermissions.get(principal.getId()))).collect(toList());
    }

    @RequestMapping(value = "/contacts/{contactId}/acls", method = PUT)
    public void createOrUpdateAcls(@PathVariable Long contactId, @RequestBody List<AclEntryDto> aclEntries) {
        Acl acl = getAcl(contactId);
        aclEntries.forEach(aclEntry -> acl.setPermissions(aclEntry.getPrincipalId(), convert(aclEntry)));
        aclService.updateAcl(acl);
    }

    @RequestMapping(value = "/contacts/{contactId}/acls/{principalId}", method = RequestMethod.DELETE)
    public void deleteAcl(@PathVariable Long contactId, @PathVariable Long principalId) {
        Acl acl = getAcl(contactId);
        acl.removePrincipal(principalId);
        aclService.updateAcl(acl);
    }

    @RequestMapping(value = "/contacts/{contactId}/emails/{emailId}", method = RequestMethod.DELETE)
    public void deleteEmail(@PathVariable Long contactId, @PathVariable Long emailId) {
        contactService.deleteEmail(emailId);
    }

    @RequestMapping(value = "/contacts/{contactId}/addresses/{addressId}", method = RequestMethod.DELETE)
    public void deleteAddress(@PathVariable Long contactId, @PathVariable Long addressId) {
        contactService.deleteAddress(addressId);
    }

    @RequestMapping(value = "/contacts/{contactId}/social_networks/{socialNetworkId}", method = RequestMethod.DELETE)
    public void deleteSocialNetworkAccount(@PathVariable Long contactId, @PathVariable Long socialNetworkId) {
        contactService.deleteSocialNetworkAccount(socialNetworkId);
    }

    @RequestMapping(value = "/emails/types", method = RequestMethod.GET)
    public List<EmailTypeDto> getEmailTypes(){
        return convertEmailTypes(EmailType.values());
    }

    @RequestMapping(value = "/telephones/types", method = RequestMethod.GET)
    public List<TelephoneTypeDto> getTelephoneTypes(){
        return convertTelephoneTypes(TelephoneType.values());
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public List<CountryDto> getCountries() {
        return dictionaryService.getCountries();
    }

    @RequestMapping(value = "/social_networks", method = RequestMethod.GET)
    public List<SocialNetworkDto> getSocialNetworks() {
        return dictionaryService.getSocialNetworks();
    }

    private Acl getAcl(Long contactId) {
        return aclService.getAcl(createIdentity(contactId));
    }

    private ObjectIdentity createIdentity(Long contactId) {
        return new ObjectIdentityImpl(contactId, ObjectTypes.CONTACT.getName());
    }
}
