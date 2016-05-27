package com.itechart.security.business.model.dto.company;

import com.itechart.security.business.model.persistent.company.BusinessSphere;

public class BusinessSphereDto {

    private Long id;
    private String description;

    public BusinessSphereDto() {
    }

    public BusinessSphereDto(BusinessSphere entity) {
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

    public BusinessSphere convert() {
        BusinessSphere result = new BusinessSphere();
        result.setId(getId());
        result.setDescription(getDescription());
        return result;
    }
}
