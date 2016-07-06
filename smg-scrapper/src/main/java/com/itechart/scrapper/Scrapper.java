package com.itechart.scrapper;

import com.itechart.scrapper.model.crm.SecuredGroupDto;
import com.itechart.scrapper.model.smg.SmgDepartment;
import com.itechart.scrapper.model.smg.SmgProfile;
import com.itechart.scrapper.utils.CrmEditor;
import com.itechart.scrapper.utils.SmgParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scrapper {

    private static final Logger log = LoggerFactory.getLogger(Scrapper.class);

    private SmgParser smgParser;
    private CrmEditor crmEditor;

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
        departmentsMap = getMapOfDepartments();
    }

    private void scrapAllUsersFromSmgToCrm(Integer smgProfileId) {
        if (smgProfileId != null) {
            exportSpecificUserFromSmgToCrm(smgProfileId);
        } else {
            importAllProfiles();
        }
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
        } catch (IOException e) {
            log.error("can't read SMG profile", e);
        }
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
