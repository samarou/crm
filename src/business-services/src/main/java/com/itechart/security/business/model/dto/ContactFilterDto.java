package com.itechart.security.business.model.dto;

import com.itechart.security.business.filter.ContactFilter;
import com.itechart.common.model.filter.dto.TextFilterDto;

public class ContactFilterDto extends TextFilterDto {

    public ContactFilter convert() {
        return super.convert(new ContactFilter());
    }
}
