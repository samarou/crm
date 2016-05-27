package com.itechart.security.business.model.dto.company;

import com.itechart.security.business.model.persistent.company.CompanyType;

public class CompanyTypeDto {

    private Long id;
    private String description;

    public CompanyTypeDto() {
    }

    public CompanyTypeDto(CompanyType entity) {
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

    public CompanyType convert() {
        CompanyType result = new CompanyType();
        result.setId(getId());
        result.setDescription(getDescription());
        return result;
    }
}
