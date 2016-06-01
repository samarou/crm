package com.itechart.security.model.util;

import com.itechart.security.model.dto.PrivilegeDto;
import com.itechart.security.model.persistent.Privilege;

public class PrivilegeConverter {

    public static PrivilegeDto convert(Privilege entity) {
        PrivilegeDto dto = new PrivilegeDto();
        dto.setId(entity.getId());
        dto.setAction(ActionConverter.convert(entity.getAction()));
        dto.setObjectType(ObjectTypeConverter.convert(entity.getObjectType()));
        return dto;
    }

    public static Privilege convert(PrivilegeDto dto) {
        Privilege entity = new Privilege();
        entity.setId(dto.getId());
        entity.setAction(ActionConverter.convert(dto.getAction()));
        entity.setObjectType(ObjectTypeConverter.convert(dto.getObjectType()));
        return entity;
    }

}
