package com.itechart.security.service;

import com.itechart.security.model.dto.GroupDto;
import com.itechart.security.model.dto.PublicGroupDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author yauheni.putsykovich
 */
public interface GroupService {

    List<GroupDto> getGroups();

    Serializable create(GroupDto group);

    void update(GroupDto convert);

    GroupDto get(Long id);

    GroupDto getGroupWithUsers(Long id);

    List<PublicGroupDto> getPublicGroups();

    void deleteById(Long id);
}
