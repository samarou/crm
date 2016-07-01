package com.itechart.scrapper;

import com.itechart.scrapper.model.crm.LoginDataDto;
import com.itechart.scrapper.model.crm.SecuredGroupDto;
import com.itechart.scrapper.model.crm.SecuredUserDto;
import com.itechart.scrapper.model.crm.SessionInfoDto;
import com.itechart.scrapper.model.crm.contact.ContactDto;
import com.itechart.scrapper.model.smg.SmgDepartment;
import com.itechart.scrapper.model.smg.SmgProfile;
import com.itechart.scrapper.parser.Parser;
import com.itechart.scrapper.parser.impl.SmgParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class Scrapper {

    private static final Logger log = LoggerFactory.getLogger(Scrapper.class);

    private static final String IMPORTED_PROFILE_IDS_FILEPATH = "./imported_profiles.txt";

    private static String BASE_URL;
    private static String GROUPS_URL;
    private static String USERS_URL;
    private static String LOGIN_URL;
    private static String CONTACT_URL;
    private static String CHECK_IF_USER_EXISTS_URL;

    private String AUTH_TOKEN_ADMIN;
    private String AUTH_TOKEN_SPECIALIST;
    private RestTemplate rest;
    private Parser smgParser;
    private Map<Integer, SecuredGroupDto> departmentsMap;
    private Set<Integer> importedProfileIDs;

    public static void main(String[] args) {
        Integer smgProfileId = null;
        if (args.length != 0) {
            smgProfileId = Integer.parseInt(args[0]);
        }
        Scrapper scrapper = new Scrapper();
        scrapper.scrapAllUsersFromSmgToCrm(smgProfileId);
    }

    private Scrapper() {
        rest = new RestTemplate();
        smgParser = new SmgParser();
        setURLs();
        AUTH_TOKEN_ADMIN = logInCRM(getCrmUser("admin"));
        AUTH_TOKEN_SPECIALIST = logInCRM(getCrmUser("manager"));
        departmentsMap = getMapOfDepartments();
        importedProfileIDs = getImportedProfileIDs();
    }

    private void setURLs(){
        BASE_URL = smgParser.getProperties().getProperty("crm.address");
        GROUPS_URL = BASE_URL + "/groups";
        USERS_URL = BASE_URL + "/users";
        LOGIN_URL = BASE_URL + "/login";
        CONTACT_URL = BASE_URL + "/contacts";
        CHECK_IF_USER_EXISTS_URL = USERS_URL + "/check";
    }

    private void scrapAllUsersFromSmgToCrm(Integer smgProfileId) {
        if (smgProfileId != null) {
            exportSpecificUserFromSmgToCrm(smgProfileId);
        } else {
            importAllProfiles();
        }
    }

    private CrmUser getCrmUser(String role) {
        CrmUser user = new CrmUser();
        user.setUserName(smgParser.getProperties().getProperty(format("%s.profile", role)));
        user.setPassword(smgParser.getProperties().getProperty(format("%s.password", role)));
        return user;
    }

    private String logInCRM(CrmUser user) {
        LoginDataDto loginDataDto = new LoginDataDto();
        loginDataDto.setUsername(user.getUserName());
        loginDataDto.setPassword(user.getPassword());
        ResponseEntity<SessionInfoDto> sessionInfoResponse = rest.postForEntity(LOGIN_URL, loginDataDto, SessionInfoDto.class);
        SessionInfoDto sessionInfo = sessionInfoResponse.getBody();
        log.info("session info: roles: {}, token: {}, username: {}", sessionInfo.getRoles(), sessionInfo.getToken(), sessionInfo.getUsername());
        return sessionInfo.getToken();
    }

    private Map<Integer, SecuredGroupDto> getMapOfDepartments() {
        List<SecuredGroupDto> oldGroups = getGroupListFromCRM(AUTH_TOKEN_ADMIN);
        List<SmgDepartment> departments;
        Map<Integer, SecuredGroupDto> map = new HashMap<>();
        try {
            departments = parseDepartments();
            for (SmgDepartment department : departments) {
                Long deptId = department.getIdOfGroupFromList(oldGroups);
                if (deptId == null) {
                    deptId = postEntity(GROUPS_URL, department.convertToCrmGroup(null), AUTH_TOKEN_ADMIN);
                }
                map.put(department.getId(), department.convertToCrmGroup(deptId));
            }
        } catch (IOException e) {
            log.error("can't parse departments {}", e);
        }
        return map;
    }

    private Set<Integer> getImportedProfileIDs() {
        Set<Integer> set = new HashSet<>();
        try (Scanner scanner = new Scanner(new File(IMPORTED_PROFILE_IDS_FILEPATH))) {
            while (scanner.hasNext()) {
                set.add(scanner.nextInt());
            }
        } catch (IOException e) {
            log.error("can't read imported profiles from file", e);
        }
        return set;
    }

    private void importAllProfiles() {
        try {
            List<SmgProfile> profiles = smgParser.getAllProfiles();
            profiles.stream()
                .filter(profile -> !wasProfileImported(profile.getProfileId()))
                .forEach(this::exportProfileToCrm);
        } catch (IOException e) {
            log.error("can't read SMG profile", e);
        } finally {
            try {
                Files.write(Paths.get(IMPORTED_PROFILE_IDS_FILEPATH), convertToStrings(importedProfileIDs), TRUNCATE_EXISTING, CREATE);
            } catch (IOException e) {
                log.error("can't write imported users to file", e);
            }
        }
    }

    private Boolean isCrmEntityExistsGetRequest(String path, String authToken) {
        ResponseEntity<Boolean> securedGroupsRequest = rest.exchange(
            path,
            HttpMethod.GET,
            createRequestEntity(null, authToken),
            Boolean.class);
        return securedGroupsRequest.getBody();
    }

    private Long postEntity(String path, Object entity, String authToken) {
        return rest.exchange(
            path,
            HttpMethod.POST,
            createRequestEntity(entity, authToken),
            Long.class).getBody();
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

    private HttpEntity<Object> createRequestEntity(Object entity, String authToken) {
        MultiValueMap<String, String> headers =
            new LinkedMultiValueMap<>();
        headers.add("X-Auth-Token", authToken);
        return new HttpEntity<>(entity, headers);
    }

    private List<SmgDepartment> parseDepartments() throws IOException {
        return smgParser.getDepartments();
    }

    private List<Integer> parseProfileIDs() throws IOException, InterruptedException {
        return smgParser.getProfileIDs();
    }

    private Boolean wasProfileImported(Integer id) {
        return importedProfileIDs.contains(id);
    }

    private void exportSpecificUserFromSmgToCrm(Integer id) {
        try {
            exportProfileToCrm(getSmgProfile(id));
        } catch (IOException e) {
            log.error("can't export user with ID={} from SMG to CRM", id);
        }
    }

    private void exportProfileToCrm(SmgProfile profile) {
        Integer id = profile.getProfileId();
        log.debug("importing user with ID={}", id);
        saveUserAndContact(profile);
        importedProfileIDs.add(id);
    }

    private SmgProfile getSmgProfile(Integer id) throws IOException {
        return smgParser.getProfile(id);
    }

    private void saveUserAndContact(SmgProfile profile) {
        if (!profile.getDomenName().isEmpty() && !isUserExistsInCRM(profile)) {
            log.info("creating contact and user in CRM with userName {}", profile.getDomenName());
            saveUser(profile);
            saveContact(profile);
        } else {
            log.info("user {} was found in CRM system", profile.getDomenName());
        }
    }

    private List<String> convertToStrings(Set<Integer> ints) {
        return ints.stream().map(Object::toString).collect(Collectors.toList());
    }

    private Boolean isUserExistsInCRM(SmgProfile user) {
        String checkingUrl = format("%s/%s", CHECK_IF_USER_EXISTS_URL, user.getDomenName());
        log.debug("checking, if user {} {} exists in CRM. URL: {}", user.getFirstNameEng(), user.getLastNameEng(), checkingUrl);
        return isCrmEntityExistsGetRequest(checkingUrl, AUTH_TOKEN_ADMIN);
    }

    private Long saveUser(SmgProfile profile) {
        SecuredUserDto user = profile.convertToUser();
        user.getGroups().add(departmentsMap.get(profile.getDeptId()));
        return sendUserToCrm(user);
    }

    private Long saveContact(SmgProfile profile) {
        ContactDto contact = profile.convertToContact();
        return sendContactToCrm(contact);
    }

    private Long sendUserToCrm(SecuredUserDto user) {
        log.debug("posting user {} {} to CRM using URL: {}", user.getFirstName(), user.getLastName(), USERS_URL);
        return postEntity(USERS_URL, user, AUTH_TOKEN_ADMIN);
    }

    private Long sendContactToCrm(ContactDto contact) {
        log.debug("posting contact {} {} to CRM using URL: {}", contact.getFirstName(), contact.getLastName(), CONTACT_URL);
        return postEntity(CONTACT_URL, contact, AUTH_TOKEN_SPECIALIST);
    }
}
