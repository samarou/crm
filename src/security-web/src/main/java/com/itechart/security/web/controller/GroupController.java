package com.itechart.security.web.controller;

import com.itechart.security.model.persistent.Group;
import com.itechart.security.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;

    @RequestMapping("/groups")
    public List<Group> fetchAll() {
        return groupService.fetchAll();
    }
}
