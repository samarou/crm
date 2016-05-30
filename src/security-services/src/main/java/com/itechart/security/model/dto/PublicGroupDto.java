package com.itechart.security.model.dto;

import com.itechart.security.model.persistent.Group;

public class PublicGroupDto {
    private Long id;
    private String name;
    private String description;

    public PublicGroupDto() {
    }

    public PublicGroupDto(Group entity) {
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

    public Group convert() {
        Group entity = new Group();
        entity.setId(getId());
        entity.setName(getName());
        entity.setDescription(getDescription());

        return entity;
    }
}
