package com.itechart.security.web.controller;

import com.itechart.security.business.model.dto.company.*;
import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.service.CompanyService;
import com.itechart.security.model.dto.AclEntryDto;
import com.itechart.security.web.model.dto.DataPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@PreAuthorize("hasAnyRole('MANAGER', 'SPECIALIST')")
public class CompanyController extends SecuredController {

    @Autowired
    private CompanyService companyService;

    @RequestMapping("/companies")
    public DataPageDto<CompanyDto> getCompanies(CompanyFilterDto filter) {
        List<CompanyDto> companies = companyService.findCompanies(filter.convert());
        DataPageDto<CompanyDto> result = new DataPageDto<>();
        result.setData(companies);
        result.setTotalCount(companyService.countCompanies(filter.convert()));
        return result;
    }

    @RequestMapping(value = "/companies/{companyId}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(#companyId, 'sample.Company', 'READ')")
    public CompanyDto get(@PathVariable Long companyId) {
        return companyService.get(companyId);
    }

    @RequestMapping(value = "/companies", method = PUT)
    @PreAuthorize("hasPermission(#company.getId(), 'sample.Company', 'WRITE')")
    public void update(@RequestBody CompanyDto company) {
        companyService.updateCompany(company);
    }

    @RequestMapping(value = "/companies", method = POST)
    public Long create(@RequestBody CompanyDto company) {
        Long companyId = companyService.saveCompany(company);
        super.createAcl(companyId);
        return companyId;
    }

    @RequestMapping(value = "/companies/{companyId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#companyId, 'sample.Company', 'DELETE')")
    public void delete(@PathVariable Long companyId) {
        super.deleteAcl(companyId);
        companyService.deleteById(companyId);
    }

    @RequestMapping("/companies/company_types")
    public List<CompanyTypeDto> getCompanyTypes() {
        return companyService.loadCompanyTypes();
    }

    @RequestMapping("/companies/business_spheres")
    public List<BusinessSphereDto> getBusinessSpheres() {
        return companyService.loadBusinessSpheres();
    }

    @RequestMapping("/companies/employee_number_categories")
    public List<EmployeeNumberCategoryDto> getEmployeeNumberCategories() {
        return companyService.loadEmployeeNumberCategories();
    }

    @RequestMapping("/companies/{companyId}/actions/{action}")
    public boolean isAllowed(@PathVariable Long companyId, @PathVariable String action) {
        return super.isAllowed(companyId, action);
    }

    @RequestMapping("/companies/{companyId}/acls")
    @PreAuthorize("hasPermission(#companyId, 'sample.Company', 'READ')")
    public List<AclEntryDto> getAcls(@PathVariable Long companyId) {
        return super.getAcls(companyId);
    }

    @RequestMapping(value = "/companies/{companyId}/acls", method = PUT)
    @PreAuthorize("hasPermission(#companyId, 'sample.Company', 'ADMIN')")
    public void createOrUpdateAcls(@PathVariable Long companyId, @RequestBody List<AclEntryDto> aclEntries) {
        super.createOrUpdateAcls(companyId, aclEntries);
    }

    @RequestMapping(value = "/companies/{companyId}/acls/{principalId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasPermission(#companyId, 'sample.Company', 'ADMIN')")
    public void deleteAcl(@PathVariable Long companyId, @PathVariable Long principalId) {
        super.deleteAcl(companyId, principalId);
    }

    @Override
    public ObjectTypes getObjectType() {
        return ObjectTypes.COMPANY;
    }
}
