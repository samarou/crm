package com.itechart.security.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itechart.security.business.dao.CompanyDao;
import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.model.dto.company.CompanyDto;
import com.itechart.security.business.service.CompanyService;

import static com.itechart.security.business.model.dto.company.CompanyDtoConverter.convert;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyDao companyDao;

	@Override
	@Transactional
	public List<CompanyDto> findCompanies(CompanyFilter filter) {
		return convert(companyDao.find(filter));
	}

	@Override
	@Transactional
	public Long saveCompany(CompanyDto company) {
		return companyDao.save(convert(company));
	}

	@Override
	@Transactional
	public CompanyDto get(Long id) {
		return convert(companyDao.get(id));
	}

	@Override
	@Transactional
	public void updateCompany(CompanyDto company) {
		companyDao.update(convert(company));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		companyDao.deleteById(id);
	}

	@Override
	@Transactional
	public int countCompanies(CompanyFilter filter) {
		return companyDao.count(filter);
	}

}
