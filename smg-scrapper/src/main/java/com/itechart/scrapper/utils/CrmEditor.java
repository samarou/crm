package com.itechart.scrapper.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.scrapper.model.crm.*;
import com.itechart.scrapper.model.crm.contact.ContactDto;
import com.itechart.scrapper.model.crm.roles.RoleDto;
import com.itechart.scrapper.model.smg.SmgProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.itechart.scrapper.utils.ScrapperProperties.getCrmUser;
import static java.lang.String.format;
import static org.springframework.util.StringUtils.isEmpty;

public class CrmEditor {

    private static final Logger log = LoggerFactory.getLogger(CrmEditor.class);

    private RestTemplate rest;

    private static String BASE_URL = ScrapperProperties.getCrmAddress();
    private static String GROUPS_URL = BASE_URL + "/groups";
    private static String ROLES_URL = BASE_URL + "/roles";
    private static String USERS_URL = BASE_URL + "/users";
    private static String LOGIN_URL = BASE_URL + "/login";
    private static String CONTACTS_URL = BASE_URL + "/contacts";
    private static String CHECK_IF_USER_EXISTS_URL = USERS_URL + "/check";
    private static String FIND_USER_URL = USERS_URL + "/get";
    private static String FIND_CONTACT_URL = CONTACTS_URL + "/email";

    private Set<Integer> importedProfileIDs;
    private List<RoleDto> roles;

    private String AUTH_TOKEN_ADMIN;
    private String AUTH_TOKEN_SPECIALIST;
    private SecuredGroupDto adminGroup;

    public CrmEditor() {
        rest = new RestTemplate();
        AUTH_TOKEN_ADMIN = logInCRM(getCrmUser("admin"));
        AUTH_TOKEN_SPECIALIST = logInCRM(getCrmUser("manager"));
        importedProfileIDs = new HashSet<>();
        adminGroup = initExecutivesGroup();
        roles = initRoles();
    }

    private String logInCRM(LoginDataDto user) {
        ResponseEntity<SessionInfoDto> sessionInfoResponse = rest.postForEntity(LOGIN_URL, user, SessionInfoDto.class);
        SessionInfoDto sessionInfo = sessionInfoResponse.getBody();
        log.info("session info: roles: {}, token: {}, username: {}", sessionInfo.getRoles(), sessionInfo.getToken(), sessionInfo.getUsername());
        return sessionInfo.getToken();
    }

    public void exportProfileToCrm(SmgProfile profile, SecuredGroupDto department) {
        if (!isEmpty(profile.getDomenName())) {
            SecuredUserDto user;
            Long contactId;
            Set<UserDefaultAclDto> newAcls = createAcls(profile, department);
            if (!isUserExistsInCRM(profile)) {
                log.info("creating contact and user in CRM with userName {}", profile.getDomenName());
                user = saveUser(profile, department, newAcls);
                contactId = saveContact(profile);
                importedProfileIDs.add(profile.getProfileId());
            } else {
                log.info("updating user in CRM with userName {}", profile.getDomenName());
                user = updateUser(profile, department, newAcls);
                contactId = updateContact(profile);
            }
            if (profile.isDepartmentManager()) {
                department.setManager(user);
            }
            if (contactId != null) {
                putContactAcls(newAcls, contactId);
            }
        }
    }

    private SecuredUserDto updateUser(SmgProfile profile, SecuredGroupDto department, Set<UserDefaultAclDto> newAcls) {
        SecuredUserDto user = createUserForSaving(profile, department, newAcls);
        SecuredUserDto originalUser = getUserFromCrm(profile.getDomenName());
        originalUser.addFields(user);
        updateUserInCrm(originalUser);
        return originalUser;
    }

    private Long updateContact(SmgProfile profile) {
        Long contactId = null;
        if (!isEmpty(profile.getEmail())) {
            ContactDto contact = profile.convertToContact();
            ContactDto originalContact = getContactFromCrm(profile.getEmail());
            if (originalContact != null) {
                originalContact.addFields(contact);
                updateContactInCrm(originalContact);
                contactId = originalContact.getId();
            }
        } else {
            log.info("profile {} has no email, not updating", profile.getDomenName());
        }
        return contactId;
    }

    private SecuredGroupDto initExecutivesGroup() {
        List<SecuredGroupDto> oldGroups = getCrmGroups();
        SecuredGroupDto group = new SecuredGroupDto();
        group.setName("Executives");
        group.setDescription("Administration of iTechArt");
        Long id = group.getIdOfGroupFromList(oldGroups);
        if (id == null) {
            id = sendGroupToCrm(group);
        }
        group.setId(id);
        return group;
    }

    public Long sendGroupToCrm(SecuredGroupDto group) {
        log.debug("posting group {} ({}) to CRM using URL: {}", group.getName(), group.getDescription(), CONTACTS_URL);
        return postEntity(GROUPS_URL, group, AUTH_TOKEN_ADMIN);
    }

    private Boolean isUserExistsInCRM(SmgProfile user) {
        String checkingUrl = format("%s/%s", CHECK_IF_USER_EXISTS_URL, user.getDomenName());
        log.debug("checking, if user {} {} exists in CRM. URL: {}", user.getFirstNameEng(), user.getLastNameEng(), checkingUrl);
        return isCrmEntityExistsGetRequest(checkingUrl, AUTH_TOKEN_ADMIN);
    }

    private SecuredUserDto saveUser(SmgProfile profile, SecuredGroupDto department, Set<UserDefaultAclDto> newAcls) {
        SecuredUserDto user = createUserForSaving(profile, department, newAcls);
        Long userId = sendUserToCrm(user);
        user.setId(userId);
        return user;
    }

    private Long saveContact(SmgProfile profile) {
        ContactDto contact = profile.convertToContact();
        return sendContactToCrm(contact);
    }

    private SecuredUserDto createUserForSaving(SmgProfile profile, SecuredGroupDto department, Set<UserDefaultAclDto> newAcls) {
        SecuredUserDto user = profile.convertToUser();
        user.setAcls(newAcls);
        Set<SecuredGroupDto> groups = user.getGroups();
        Set<RoleDto> userRoles = user.getRoles();
        Boolean isDepartmentManager = profile.isDepartmentManager();
        groups.add(department);
        if (isDepartmentManager) {
            groups.add(adminGroup);
        }
        userRoles.add(RoleDto.getManagerOrSpecialistFromList(isDepartmentManager, roles));
        return user;
    }

    private Set<UserDefaultAclDto> createAcls(SmgProfile profile, SecuredGroupDto department) {
        Set<UserDefaultAclDto> acls = new HashSet<>();
        Boolean isDepartmentManager = profile.isDepartmentManager();
        acls.add(department.convertToAcl(isDepartmentManager));
        if (isDepartmentManager) {
            acls.add(adminGroup.convertToAcl(false));
        } else {
            if (department.getManager() != null) {
                acls.add(department.getManager().convertToAcl(true));
            }
        }
        return acls;
    }

    private Long sendUserToCrm(SecuredUserDto user) {
        log.debug("posting user {} {} to CRM using URL: {}", user.getFirstName(), user.getLastName(), USERS_URL);
        return postEntity(USERS_URL, user, AUTH_TOKEN_ADMIN);
    }

    private Long sendContactToCrm(ContactDto contact) {
        log.debug("posting contact {} {} to CRM using URL: {}", contact.getFirstName(), contact.getLastName(), CONTACTS_URL);
        return postEntity(CONTACTS_URL, contact, AUTH_TOKEN_SPECIALIST);
    }

    private void putContactAcls(Set<UserDefaultAclDto> acls, Long contactId) {
        log.debug("posting acls for contact with id {} to CRM", contactId);
        putEntity(format("%s/%d/acls", CONTACTS_URL, contactId), acls, AUTH_TOKEN_SPECIALIST);
    }

    private Long updateUserInCrm(SecuredUserDto user) {
        log.debug("updating user {} {} in CRM using URL: {}", user.getFirstName(), user.getLastName(), USERS_URL);
        return putEntity(USERS_URL, user, AUTH_TOKEN_ADMIN);
    }

    private Long updateContactInCrm(ContactDto contact) {
        log.debug("updating contact {} {} in CRM using URL: {}", contact.getFirstName(), contact.getLastName(), CONTACTS_URL);
        return putEntity(CONTACTS_URL, contact, AUTH_TOKEN_SPECIALIST);
    }

    private SecuredUserDto getUserFromCrm(String userName) {
        log.debug("getting user {} from CRM using URL: {}", userName, FIND_USER_URL);
        return getEntity(FIND_USER_URL + "/" + userName, AUTH_TOKEN_ADMIN, SecuredUserDto.class);
    }

    private ContactDto getContactFromCrm(String email) {
        log.debug("getting contact with email {}", email);
        ContactDto contact = null;
        ObjectMapper mapper = new ObjectMapper();
        String json = getEntity(FIND_CONTACT_URL + "/" + email, AUTH_TOKEN_SPECIALIST, String.class);
        try {
            contact = mapper.readValue(json, ContactDto.class);
        } catch (IOException e) {
            log.error("can't convert contact", e);
        }
        return contact;
    }

    private Boolean isCrmEntityExistsGetRequest(String path, String authToken) {
        ResponseEntity<Boolean> securedGroupsRequest = rest.exchange(
            path,
            HttpMethod.GET,
            createRequestEntity(null, authToken),
            Boolean.class);
        return securedGroupsRequest.getBody();
    }

    public List<SecuredGroupDto> getCrmGroups() {
        return getGroupListFromCRM(AUTH_TOKEN_ADMIN);
    }

    private List<RoleDto> initRoles() {
        ParameterizedTypeReference<List<RoleDto>> typeRef
            = new ParameterizedTypeReference<List<RoleDto>>() {
        };
        ResponseEntity<List<RoleDto>> rolesRequest = rest.exchange(
            ROLES_URL,
            HttpMethod.GET,
            createRequestEntity(null, AUTH_TOKEN_ADMIN),
            typeRef);
        return rolesRequest.getBody();
    }

    private List<SecuredGroupDto> getGroupListFromCRM(String authToken) {
        ParameterizedTypeReference<List<SecuredGroupDto>> typeRef
            = new ParameterizedTypeReference<List<SecuredGroupDto>>() {
        };
        ResponseEntity<List<SecuredGroupDto>> securedGroupsRequest = rest.exchange(
            GROUPS_URL,
            HttpMethod.GET,
            createRequestEntity(null, authToken),
            typeRef);
        return securedGroupsRequest.getBody();
    }

    private Long postEntity(String path, Object entity, String authToken) {
        return rest.exchange(
            path,
            HttpMethod.POST,
            createRequestEntity(entity, authToken),
            Long.class).getBody();
    }

    private Long putEntity(String path, Object entity, String authToken) {
        return rest.exchange(
            path,
            HttpMethod.PUT,
            createRequestEntity(entity, authToken),
            Long.class).getBody();
    }

    private <T> T getEntity(String path, String authToken, Class<T> type) {
        return rest.exchange(
            path,
            HttpMethod.GET,
            createRequestEntity(null, authToken),
            type).getBody();
    }

    private HttpEntity<Object> createRequestEntity(Object entity, String authToken) {
        MultiValueMap<String, String> headers =
            new LinkedMultiValueMap<>();
        headers.add("X-Auth-Token", authToken);
        headers.add("Accept", "application/json");
        return new HttpEntity<>(entity, headers);
    }

    public Boolean wasProfileImported(Integer id) {
        return importedProfileIDs.contains(id);
    }
}
