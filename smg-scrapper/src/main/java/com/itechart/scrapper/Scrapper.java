package com.itechart.scrapper;

import com.itechart.scrapper.model.crm.SecuredGroupDto;
import com.itechart.scrapper.model.smg.SmgDepartment;
import com.itechart.scrapper.model.smg.SmgProfile;
import com.itechart.scrapper.utils.CrmEditor;
import com.itechart.scrapper.utils.SmgParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Scrapper {

    private static final Logger log = LoggerFactory.getLogger(Scrapper.class);

    private SmgParser smgParser;
    private CrmEditor crmEditor;

    private static final String IMPORTED_PROFILE_IDS_FILEPATH = "./imported_profiles.txt";

    private static Map<Integer, SecuredGroupDto> departmentsMap;

    public static void main(String[] args) {
        Integer smgProfileId = null;
        if (args.length != 0) {
            smgProfileId = Integer.parseInt(args[0]);
        }
        Scrapper scrapper = new Scrapper();
        scrapper.scrapAllUsersFromSmgToCrm(smgProfileId);
    }

    private Scrapper() {
        smgParser = new SmgParser();
        crmEditor = new CrmEditor();
        crmEditor.initImportedProfiles(getImportedProfileIDs());
        departmentsMap = getMapOfDepartments();
    }

    private void scrapAllUsersFromSmgToCrm(Integer smgProfileId) {
        if (smgProfileId != null) {
            exportSpecificUserFromSmgToCrm(smgProfileId);
        } else {
            importAllProfiles();
        }
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

    private Map<Integer, SecuredGroupDto> getMapOfDepartments() {
        List<SecuredGroupDto> oldGroups = crmEditor.getCrmGroups();
        List<SmgDepartment> departments;
        Map<Integer, SecuredGroupDto> map = new HashMap<>();
        try {
            departments = parseDepartments();
            for (SmgDepartment department : departments) {
                Long deptId = department.getIdOfGroupFromList(oldGroups);
                if (deptId == null) {
                    deptId = crmEditor.sendGroupToCrm(department.convertToCrmGroup(null));
                }
                map.put(department.getId(), department.convertToCrmGroup(deptId));
            }
        } catch (IOException e) {
            log.error("can't parse departments {}", e);
        }
        return map;
    }

    private void importAllProfiles() {
        try {
            List<SmgProfile> profiles = smgParser.getAllProfiles();
            importDepManagers(profiles);
            importEverybody(profiles);
        } catch (Exception e) {
            try {
                Files.write(Paths.get(IMPORTED_PROFILE_IDS_FILEPATH), convertToStrings(crmEditor.getImportedProfileIDs()), APPEND, CREATE);
            } catch (IOException ex) {
                log.error("can't write ids of imported profiles to file", ex);
            }
            throw e;
        }
        try {
            Files.delete(Paths.get(IMPORTED_PROFILE_IDS_FILEPATH));
        } catch (IOException e) {
            log.error("can't delete file because it doesn't exist");
        }
    }

    private List<String> convertToStrings(Set<Integer> ints) {
        return ints.stream().map(Object::toString).collect(Collectors.toList());
    }

    private void importDepManagers(List<SmgProfile> profiles) {
        profiles.stream()
            .filter(profile -> profile.isDepartmentManager() && !crmEditor.wasProfileImported(profile.getProfileId()))
            .forEach(this::exportProfileToCrm);
    }

    private void importEverybody(List<SmgProfile> profiles) {
        profiles.stream()
            .filter(profile -> !profile.isDepartmentManager() && !crmEditor.wasProfileImported(profile.getProfileId()))
            .forEach(this::exportProfileToCrm);
    }

    private void exportProfileToCrm(SmgProfile profile) {
        crmEditor.exportProfileToCrm(profile, departmentsMap.get(profile.getDeptId()));
    }

    private List<SmgDepartment> parseDepartments() throws IOException {
        return smgParser.getDepartments();
    }

    private void exportSpecificUserFromSmgToCrm(Integer id) {
        try {
            SmgProfile profile = smgParser.getProfile(id);
            crmEditor.exportProfileToCrm(profile, departmentsMap.get(profile.getDeptId()));
        } catch (IOException e) {
            log.error("can't export user with ID={} from SMG to CRM", id);
        }
    }
}
