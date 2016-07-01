package com.itechart.scrapper.model.crm;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SecuredGroupDto extends PublicGroupDto{
    private List<PublicUserDto> members;
}