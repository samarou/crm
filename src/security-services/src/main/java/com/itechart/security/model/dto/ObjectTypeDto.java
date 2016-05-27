package com.itechart.security.model.dto;

import com.itechart.security.model.persistent.ObjectType;

public class ObjectTypeDto {
    private Long id;
    private String name;
    private String description;

    public ObjectTypeDto() {
    }

    public ObjectTypeDto(ObjectType entity) {
        setId(entity.getId());
        setName(entity.getName());
        setDescription(entity.getDescription());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectType convert() {
        ObjectType entity = new ObjectType();
        entity.setId(getId());
        entity.setName(getName());
        entity.setDescription(getDescription());
        return entity;
    }
}
