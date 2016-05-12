package com.itechart.security.business.service;

import java.util.List;

import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.model.dto.company.CompanyDto;

public interface CompanyService {
	
	List<CompanyDto> findCompanies(CompanyFilter filter);
	
	Long saveCompany(CompanyDto company);
	
	CompanyDto get(Long id);
	
	void updateCompany(CompanyDto company);
	
	void deleteById(Long id);
	
	int countCompanies(CompanyFilter filter);
}
