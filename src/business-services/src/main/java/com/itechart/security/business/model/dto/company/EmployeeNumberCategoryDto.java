package com.itechart.security.business.model.dto.company;

import com.itechart.security.business.model.persistent.company.EmployeeNumberCategory;

public class EmployeeNumberCategoryDto {

    private Long id;
    private String description;

    public EmployeeNumberCategoryDto() {
    }

    public EmployeeNumberCategoryDto(EmployeeNumberCategory entity) {
        if (entity != null) {
            setId(entity.getId());
            setDescription(entity.getDescription());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmployeeNumberCategory convert() {
        EmployeeNumberCategory result = new EmployeeNumberCategory();
        result.setId(getId());
        result.setDescription(getDescription());
        return result;
    }
}
