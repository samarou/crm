package com.itechart.security.business.dao;

import java.util.List;

import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.model.persistent.company.BusinessSphere;
import com.itechart.security.business.model.persistent.company.Company;
import com.itechart.security.business.model.persistent.company.CompanyType;
import com.itechart.security.business.model.persistent.company.EmployeeNumberCategory;

public interface CompanyDao {

    Long save(Company company);

    void update(Company company);

    Company get(Long id);

    void deleteById(Long id);

    List<Company> find(CompanyFilter filter);

    int count(CompanyFilter filter);

    List<CompanyType> loadCompanyTypes();

    List<BusinessSphere> loadBusinessSpheres();

    List<EmployeeNumberCategory> loadEmployeeNumberCategories();
}
