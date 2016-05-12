package com.itechart.security.business.dao;

import java.util.List;

import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.model.persistent.company.Company;

public interface CompanyDao {
	
	Long save(Company company);
	
	List<Company> loadAll();
	
	void update(Company company);
	
	Company get(Long id);
	
	void deleteById(Long id);
	
	List<Company> find(CompanyFilter filter);
	
	int count(CompanyFilter filter);
}
