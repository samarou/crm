package com.itechart.security.business.model.dto.utils;

import com.itechart.common.model.util.FilterConverter;
import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.model.dto.company.*;
import com.itechart.security.business.model.persistent.company.BusinessSphere;
import com.itechart.security.business.model.persistent.company.Company;
import com.itechart.security.business.model.persistent.company.CompanyType;
import com.itechart.security.business.model.persistent.company.EmployeeNumberCategory;

import java.util.Optional;

public class CompanyConverter {

    public static CompanyFilter convert(CompanyFilterDto dto) {
        CompanyFilter filter = FilterConverter.convert(new CompanyFilter(), dto);
        filter.setEmployeeNumberCategoryId(dto.getEmployeeNumberCategoryId());
        return filter;
    }

    public static BusinessSphereDto convert(BusinessSphere entity) {
        return Optional.ofNullable(entity).map(businessSphere -> {
            BusinessSphereDto dto = new BusinessSphereDto();
            dto.setId(businessSphere.getId());
            dto.setDescription(businessSphere.getDescription());
            return dto;
        }).orElse(null);
    }

    public static BusinessSphere convert(BusinessSphereDto dto) {
        BusinessSphere entity = new BusinessSphere();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static CompanyTypeDto convert(CompanyType entity) {
        return Optional.ofNullable(entity).map(companyType -> {
            CompanyTypeDto dto = new CompanyTypeDto();
            dto.setId(companyType.getId());
            dto.setDescription(companyType.getDescription());
            return dto;
        }).orElse(null);
    }

    public static CompanyType convert(CompanyTypeDto dto) {
        CompanyType entity = new CompanyType();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static EmployeeNumberCategoryDto convert(EmployeeNumberCategory entity) {
        return Optional.ofNullable(entity).map(employeeNumberCategory -> {
            EmployeeNumberCategoryDto dto = new EmployeeNumberCategoryDto();
            dto.setId(employeeNumberCategory.getId());
            dto.setDescription(employeeNumberCategory.getDescription());
            return dto;
        }).orElse(null);
    }

    public static EmployeeNumberCategory convert(EmployeeNumberCategoryDto dto) {
        EmployeeNumberCategory entity = new EmployeeNumberCategory();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static CompanyDto convert(Company entity) {
        CompanyDto dto = new CompanyDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setCompanyType(convert(entity.getCompanyType()));
        dto.setBusinessSphere(convert(entity.getBusinessSphere()));
        dto.setEmployeeNumberCategory(convert(entity.getEmployeeNumberCategory()));
        dto.setCommentary(entity.getCommentary());
        return dto;
    }

    public static Company convert(CompanyDto dto) {
        Company entity = new Company();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setCompanyType(convert(dto.getCompanyType()));
        entity.setBusinessSphere(convert(dto.getBusinessSphere()));
        entity.setEmployeeNumberCategory(convert(dto.getEmployeeNumberCategory()));
        entity.setCommentary(dto.getCommentary());
        return entity;
    }

}
