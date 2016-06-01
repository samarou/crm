package com.itechart.security.model.dto;

import java.util.List;

public class SecuredGroupDto extends PublicGroupDto {

    private List<PublicUserDto> members;

    public SecuredGroupDto() {
    }

    public List<PublicUserDto> getMembers() {
        return members;
    }

    public void setMembers(List<PublicUserDto> members) {
        this.members = members;
    }

}