package com.itechart.security.business.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.itechart.security.business.dao.CompanyDao;
import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.model.persistent.company.BusinessSphere;
import com.itechart.security.business.model.persistent.company.Company;
import com.itechart.security.business.model.persistent.company.CompanyType;
import com.itechart.security.business.model.persistent.company.EmployeeNumberCathegory;
import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;

@Repository
public class CompanyDaoImpl extends BaseHibernateDao<Company> implements CompanyDao {

	@Override
	@AclFilter(@AclFilterRule(type = Company.class, permissions = {Permission.READ}))
    public List<Company> loadAll() {
		return super.loadAll();
	}
	
	@Override
	public Long save(Company company) {
		return (Long) super.save(company);
	}

	@Override
	public Company get(Long id) {
		return super.get(id);
	}

	@Override
	@AclFilter(@AclFilterRule(type = Company.class, permissions = {Permission.READ}))
	public int count(CompanyFilter filter) {
		return getHibernateTemplate().executeWithNativeSession(session -> {
			Criteria criteria = createFilterCriteria(session, filter);
            criteria.setProjection(Projections.rowCount());
            return ((Number) criteria.uniqueResult()).intValue();
		});
	}
	

	@Override
	public List<CompanyType> loadCompanyTypes() {
		return getHibernateTemplate().loadAll(CompanyType.class);
	}

	@Override
	public List<BusinessSphere> loadBusinessSpheres() {
		return getHibernateTemplate().loadAll(BusinessSphere.class);
	}

	@Override
	public List<EmployeeNumberCathegory> loadEmployeeNumberCathegories() {
		return getHibernateTemplate().loadAll(EmployeeNumberCathegory.class);
	}
	
	@Override
	@AclFilter(@AclFilterRule(type = Company.class, permissions = {Permission.READ}))
	public List<Company> find(CompanyFilter filter) {
		return getHibernateTemplate().executeWithNativeSession(session -> {
            Criteria criteria = createFilterCriteria(session, filter);
            return executePagingDistinctCriteria(session, criteria, filter);
        });
	}
	
	private Criteria createFilterCriteria(Session session, CompanyFilter filter) {
		Criteria criteria = session.createCriteria(Company.class, "company");
		if (StringUtils.hasText(filter.getText())) {
			criteria.add(Restrictions.ilike("company.name", filter.getText()));
		}
		return criteria;
	}

}
