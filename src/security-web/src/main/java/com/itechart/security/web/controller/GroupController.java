package com.itechart.security.web.controller;

import com.itechart.security.service.GroupService;
import com.itechart.security.web.model.dto.GroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.itechart.security.web.model.dto.Converter.convert;
import static com.itechart.security.web.model.dto.Converter.convertGroups;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author yauheni.putsykovich
 */
@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @RequestMapping("/groups")
    public Set<GroupDto> getGroups() {
        return convertGroups(new HashSet<>(groupService.getGroups()));
    }

    @RequestMapping(value = "/groups", method = POST)
    public Serializable create(@RequestBody GroupDto group) {
        return groupService.create(convert(group));
    }

    @RequestMapping(value = "/groups", method = PUT)
    public void update(@RequestBody GroupDto dto) {
        groupService.update(convert(dto));
    }
}
