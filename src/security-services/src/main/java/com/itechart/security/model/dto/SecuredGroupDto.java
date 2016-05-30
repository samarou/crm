package com.itechart.security.model.dto;

import com.itechart.security.model.persistent.Group;

import java.util.List;

public class SecuredGroupDto extends PublicGroupDto {

    private List<PublicUserDto> members;

    public SecuredGroupDto() {
    }

    public SecuredGroupDto(Group entity) {
        super(entity);
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