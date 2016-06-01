package com.itechart.security.model.util;

import com.itechart.security.model.dto.ActionDto;
import com.itechart.security.model.persistent.Action;

public class ActionConverter {

    public static ActionDto convert(Action entity) {
        ActionDto dto = new ActionDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public static Action convert(ActionDto dto) {
        Action entity = new Action();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

}
