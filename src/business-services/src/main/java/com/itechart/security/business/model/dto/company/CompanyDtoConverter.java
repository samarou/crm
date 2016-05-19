package com.itechart.security.business.model.dto.company;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.itechart.security.business.model.persistent.company.BusinessSphere;
import com.itechart.security.business.model.persistent.company.Company;
import com.itechart.security.business.model.persistent.company.CompanyType;
import com.itechart.security.business.model.persistent.company.EmployeeNumberCategory;

public class CompanyDtoConverter {

    public static CompanyDto convert(Company company) {
        CompanyDto result = new CompanyDto();
        result.setId(company.getId());
        result.setName(company.getName());
        result.setLogoUrl(company.getLogoUrl());
        result.setCompanyType(convert(company.getCompanyType()));
        result.setBusinessSphere(convert(company.getBusinessSphere()));
        result.setEmployeeNumberCategory(convert(company.getEmployeeNumberCategory()));
        result.setCommentary(company.getCommentary());
        return result;
    }

    // TODO implement a single generic list processor
    public static List<CompanyDto> convert(List<Company> companies) {
        if (CollectionUtils.isEmpty(companies)) {
            return Collections.emptyList();
        }
        return companies.stream().map(CompanyDtoConverter::convert).collect(Collectors.toList());
    }

    public static List<BusinessSphereDto> convertBusinessSpheres(List<BusinessSphere> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        return categories.stream().map(CompanyDtoConverter::convert).collect(Collectors.toList());
    }

    private static BusinessSphereDto convert(BusinessSphere businessSphere) {
        if (businessSphere == null) {
            return null;
        }
        BusinessSphereDto result = new BusinessSphereDto();
        result.setId(businessSphere.getId());
        result.setDescription(businessSphere.getDescription());
        return result;
    }

    public static List<CompanyTypeDto> convertCompanyTypes(List<CompanyType> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        return categories.stream().map(CompanyDtoConverter::convert).collect(Collectors.toList());
    }

    private static CompanyTypeDto convert(CompanyType companyType) {
        if (companyType == null) {
            return null;
        }
        CompanyTypeDto result = new CompanyTypeDto();
        result.setId(companyType.getId());
        result.setDescription(companyType.getDescription());
        return result;
    }

    public static List<EmployeeNumberCategoryDto> convertEmployeeNumberCategories(
            List<EmployeeNumberCategory> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }
        return categories.stream().map(CompanyDtoConverter::convert).collect(Collectors.toList());
    }

    private static EmployeeNumberCategoryDto convert(EmployeeNumberCategory employeeNumberCategory) {
        if (employeeNumberCategory == null) {
            return null;
        }
        EmployeeNumberCategoryDto result = new EmployeeNumberCategoryDto();
        result.setId(employeeNumberCategory.getId());
        result.setDescription(employeeNumberCategory.getDescription());
        return result;
    }

    public static Company convert(CompanyDto company) {
        Company result = new Company();
        result.setId(company.getId());
        result.setName(company.getName());
        result.setLogoUrl(company.getLogoUrl());
        result.setCompanyType(convert(company.getCompanyType()));
        result.setBusinessSphere(convert(company.getBusinessSphere()));
        result.setEmployeeNumberCategory(convert(company.getEmployeeNumberCategory()));
        result.setCommentary(company.getCommentary());
        return result;
    }

    private static BusinessSphere convert(BusinessSphereDto businessSphere) {
        if (businessSphere == null) {
            return null;
        }
        BusinessSphere result = new BusinessSphere();
        result.setId(businessSphere.getId());
        result.setDescription(businessSphere.getDescription());
        return result;
    }

    private static CompanyType convert(CompanyTypeDto companyType) {
        if (companyType == null) {
            return null;
        }
        CompanyType result = new CompanyType();
        result.setId(companyType.getId());
        result.setDescription(companyType.getDescription());
        return result;
    }

    private static EmployeeNumberCategory convert(EmployeeNumberCategoryDto employeeNumberCategory) {
        if (employeeNumberCategory == null) {
            return null;
        }
        EmployeeNumberCategory result = new EmployeeNumberCategory();
        result.setId(employeeNumberCategory.getId());
        result.setDescription(employeeNumberCategory.getDescription());
        return result;
    }
}
