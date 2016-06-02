package com.itechart.security.model.util;

import com.itechart.security.model.dto.PublicGroupDto;
import com.itechart.security.model.dto.SecuredGroupDto;
import com.itechart.security.model.persistent.Group;

public class GroupConverter {

    public static PublicGroupDto convertToPublicDto(Group entity) {
        PublicGroupDto dto = new PublicGroupDto();
        populatePublicData(entity, dto);
        return dto;
    }

    public static SecuredGroupDto convertToSecuredDto(Group entity) {
        SecuredGroupDto dto = new SecuredGroupDto();
        populatePublicData(entity, dto);
        return dto;
    }

    private static void populatePublicData(Group entity, PublicGroupDto dto) {
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
    }

    public static Group convert(PublicGroupDto dto) {
        Group entity = new Group();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

}
