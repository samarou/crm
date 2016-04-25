package com.itechart.security.web.model.dto;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
public class GroupDto {
    private Long id;
    private String name;
    private String description;

    public List<PublicUserDto> getMembers() {
        return members;
    }

    private List<PublicUserDto> members;

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

    public void setMembers(List<PublicUserDto> members) {
        this.members = members;
    }
}
