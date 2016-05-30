package com.itechart.security.service;

import com.itechart.security.model.dto.SecuredGroupDto;
import com.itechart.security.model.dto.PublicGroupDto;

import java.io.Serializable;
import java.util.List;

/**
 * @author yauheni.putsykovich
 */
public interface GroupService {

    List<SecuredGroupDto> getGroups();

    Serializable create(SecuredGroupDto group);

    void update(SecuredGroupDto convert);

    SecuredGroupDto get(Long id);

    SecuredGroupDto getGroupWithUsers(Long id);

    List<PublicGroupDto> getPublicGroups();

    void deleteById(Long id);
}
