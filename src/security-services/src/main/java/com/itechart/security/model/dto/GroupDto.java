package com.itechart.security.model.dto;

import com.itechart.security.model.persistent.Group;

import java.util.List;

public class GroupDto {
    private Long id;
    private String name;
    private String description;
    private List<PublicUserDto> members;

    public GroupDto() {
    }

    public GroupDto(Group entity) {
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

    public List<PublicUserDto> getMembers() {
        return members;
    }

    public void setMembers(List<PublicUserDto> members) {
        this.members = members;
    }

    public Group convert() {
        Group entity = new Group();
        entity.setId(getId());
        entity.setName(getName());
        entity.setDescription(getDescription());

        return entity;
    }
}