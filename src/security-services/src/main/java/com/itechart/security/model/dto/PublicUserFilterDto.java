package com.itechart.security.model.dto;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.common.model.filter.dto.TextFilterDto;

public class PublicUserFilterDto extends TextFilterDto {

    public UserFilter convert() {
        return super.convert(new UserFilter());
    }
}
