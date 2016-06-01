package com.itechart.security.model.dto;

import com.itechart.security.core.model.SecurityPrivilege;
import com.itechart.security.model.persistent.Privilege;
import com.itechart.security.model.util.ActionConverter;
import com.itechart.security.model.util.ObjectTypeConverter;

public class PrivilegeDto implements SecurityPrivilege {

    private Long id;
    private ObjectTypeDto objectType;
    private ActionDto action;

    public PrivilegeDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ObjectTypeDto getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectTypeDto objectType) {
        this.objectType = objectType;
    }

    public ActionDto getAction() {
        return action;
    }

    public void setAction(ActionDto action) {
        this.action = action;
    }

    public String getObjectTypeName() {
        return objectType != null ? objectType.getName() : null;
    }

    public String getActionName() {
        return action != null ? action.getName() : null;
    }

    public Privilege convert() {
        Privilege entity = new Privilege();
        entity.setId(getId());
        entity.setAction(ActionConverter.convert(getAction()));
        entity.setObjectType(ObjectTypeConverter.convert(getObjectType()));
        return entity;
    }
}
