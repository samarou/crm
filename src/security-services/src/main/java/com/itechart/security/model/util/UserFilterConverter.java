package com.itechart.security.model.util;

import com.itechart.common.model.util.FilterConverter;
import com.itechart.security.model.dto.PublicUserFilterDto;
import com.itechart.security.model.dto.SecuredUserFilterDto;
import com.itechart.security.model.filter.UserFilter;

public class UserFilterConverter {

    public static UserFilter convert(PublicUserFilterDto dto) {
        return FilterConverter.convert(new UserFilter(), dto);
    }

    public static UserFilter convert(SecuredUserFilterDto dto) {
        UserFilter filter = FilterConverter.convert(new UserFilter(), dto);
        filter.setGroupId(dto.getGroupId());
        filter.setRoleId(dto.getRoleId());
        filter.setActive(dto.isActive());
        return filter;
    }


}
