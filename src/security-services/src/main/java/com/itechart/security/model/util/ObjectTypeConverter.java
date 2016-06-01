package com.itechart.security.model.util;

import com.itechart.security.model.dto.ObjectTypeDto;
import com.itechart.security.model.persistent.ObjectType;

public class ObjectTypeConverter {

    public static ObjectTypeDto convert(ObjectType entity) {
        ObjectTypeDto dto = new ObjectTypeDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public static ObjectType convert(ObjectTypeDto dto) {
        ObjectType entity = new ObjectType();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
