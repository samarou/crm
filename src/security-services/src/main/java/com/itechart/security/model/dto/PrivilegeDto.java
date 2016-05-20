package com.itechart.security.model.dto;

public class PrivilegeDto {
    private Long id;
    private ObjectTypeDto objectType;
    private ActionDto action;

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
}
