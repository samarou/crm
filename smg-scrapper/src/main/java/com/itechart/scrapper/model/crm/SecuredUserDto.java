package com.itechart.scrapper.model.crm;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class SecuredUserDto extends PublicUserDto{
    private String password;
    private boolean active;
    private Set<SecuredGroupDto> groups;
}
