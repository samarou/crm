package com.itechart.security.business.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by yauheni.putsykovich on 01.06.2016.
 */

@Setter
@Getter
public class TaskFilter extends PagingFilter {
    @Override
    public void setSortProperty(String sortProperty) {
        String correctPropertyName;
        switch (sortProperty) {
            case "priority":
            case "status":
                correctPropertyName = sortProperty + ".id";
                break;
            default:
                correctPropertyName = sortProperty;
        }
        super.setSortProperty(correctPropertyName);
    }
}
