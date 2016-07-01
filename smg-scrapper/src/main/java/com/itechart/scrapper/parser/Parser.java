package com.itechart.scrapper.parser;

import com.itechart.scrapper.model.smg.SmgDepartment;
import com.itechart.scrapper.model.smg.SmgProfile;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public interface Parser {
    Properties getProperties();

    SmgProfile getProfile(Integer url) throws IOException;

    List<Integer> getProfileIDsByDeptId(Integer deptId) throws IOException, InterruptedException;

    List<Integer> getProfileIDs() throws IOException, InterruptedException;

    List<SmgDepartment> getDepartments() throws IOException;

    List<SmgProfile> getAllProfiles() throws IOException;
}
