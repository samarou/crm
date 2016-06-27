package com.itechart.scraper.parser;

import com.itechart.scraper.model.smg.SmgDepartment;
import com.itechart.scraper.model.smg.SmgProfile;

import java.io.IOException;
import java.util.List;

public interface Parser {
    SmgProfile getProfile(Integer url) throws IOException;
    List<SmgProfile> getProfilesByDeptId(Integer deptId) throws IOException, InterruptedException;
    List<SmgDepartment> getDepartments() throws IOException;
}
