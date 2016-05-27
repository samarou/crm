package com.itechart.security.model.dto;

public class CompanyFilterDto extends TextFilterDto {

    private Long employeeNumberCategoryId;

    public Long getEmployeeNumberCategoryId() {
        return employeeNumberCategoryId;
    }

    public void setEmployeeNumberCategoryId(Long employeeNumberCategoryId) {
        this.employeeNumberCategoryId = employeeNumberCategoryId;
    }
}
