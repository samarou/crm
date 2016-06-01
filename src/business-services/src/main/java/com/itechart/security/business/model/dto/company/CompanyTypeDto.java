package com.itechart.security.business.model.dto.company;

public class CompanyTypeDto {

    private Long id;
    private String description;

    public CompanyTypeDto() {
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

}
