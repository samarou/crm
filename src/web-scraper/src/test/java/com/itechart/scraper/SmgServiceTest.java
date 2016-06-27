package com.itechart.scraper;

import com.itechart.scraper.model.smg.SmgDepartment;
import com.itechart.scraper.model.smg.SmgProfile;
import com.itechart.scraper.parser.Parser;
import com.itechart.scraper.parser.impl.SmgProfileParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SmgServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SmgServiceTest.class);

    @Test
    public void parseSmgTest() {
        Parser smgService = new SmgProfileParser();
        List<SmgProfile> profiles = new ArrayList<>();
        List<SmgDepartment> departments = new ArrayList<>();
        try {
            log.info("SMG profile:\n{}", smgService.getProfile(828));

            //List<SmgProfile> profiles = smgService.getProfilesByDeptId(1);
            departments = smgService.getDepartments();
            profiles = new ArrayList<>();
            for (SmgDepartment dept : departments) {
                smgService = new SmgProfileParser();
                profiles.addAll(smgService.getProfilesByDeptId(dept.getId()));
            }
        } catch (IOException | InterruptedException e) {
            log.error("can't read SMG profile",e);
        } finally {
            Set<String> groups = new HashSet<>();
            for (SmgProfile profile : profiles) {
                String group = profile.getGroup();
                if (group != null) {
                    groups.add(group);
                }
            }
            log.info("SMG groups:\n{}", groups);
            log.info("SMG profiles:\n{}", profiles.size());
            log.info("SMG departments:\n{}", departments);
        }
    }
}
