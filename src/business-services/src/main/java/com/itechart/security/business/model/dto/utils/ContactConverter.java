package com.itechart.security.business.model.dto.utils;

import com.itechart.common.model.util.FilterConverter;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactFilterDto;

public class ContactConverter {

    public static ContactFilter convert(ContactFilterDto dto) {
        return FilterConverter.convert(new ContactFilter(), dto);
    }

}
