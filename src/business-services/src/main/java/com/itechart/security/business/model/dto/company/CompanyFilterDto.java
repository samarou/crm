package com.itechart.security.business.model.dto.company;

import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.common.model.filter.dto.TextFilterDto;

public class CompanyFilterDto extends TextFilterDto {

    private Long employeeNumberCategoryId;

    public Long getEmployeeNumberCategoryId() {
        return employeeNumberCategoryId;
    }

    public void setEmployeeNumberCategoryId(Long employeeNumberCategoryId) {
        this.employeeNumberCategoryId = employeeNumberCategoryId;
    }

    public CompanyFilter convert() {
        CompanyFilter result = super.convert(new CompanyFilter());
        result.setEmployeeNumberCategoryId(getEmployeeNumberCategoryId());
        return result;
    }
}
