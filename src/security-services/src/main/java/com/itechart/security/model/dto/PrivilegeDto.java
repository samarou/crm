package com.itechart.security.model.dto;

import com.itechart.security.core.model.SecurityPrivilege;
import com.itechart.security.model.persistent.Privilege;

public class PrivilegeDto implements SecurityPrivilege {

    private Long id;
    private ObjectTypeDto objectType;
    private ActionDto action;

    public PrivilegeDto() {
    }

    public PrivilegeDto(Privilege entity) {
        setId(getId());
        setAction(new ActionDto(entity.getAction()));
        setObjectType(new ObjectTypeDto(entity.getObjectType()));
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
        entity.setAction(getAction().convert());
        entity.setObjectType(getObjectType().convert());
        return entity;
    }
}
