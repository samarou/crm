package com.itechart.security.business.dao.company.impl;

import com.itechart.common.dao.impl.BaseHibernateDao;
import com.itechart.security.business.dao.company.CompanyDao;
import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.model.persistent.company.Company;
import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class CompanyDaoImpl extends BaseHibernateDao<Company, Long, CompanyFilter> implements CompanyDao {

    @AclFilter(@AclFilterRule(type = Company.class, permissions = { Permission.READ }))
    public Company get(Long id) {
        return super.get(id);
    }

    @AclFilter(@AclFilterRule(type = Company.class, permissions = { Permission.READ }))
    public List<Company> find(CompanyFilter filter) {
        return super.find(filter);
    }

    @AclFilter(@AclFilterRule(type = Company.class, permissions = { Permission.READ }))
    public int count(CompanyFilter filter) {
        return super.count(filter);
    }

    protected Criteria createFilterCriteria(Session session, CompanyFilter filter) {
        Criteria criteria = session.createCriteria(Company.class, "company");
        if (StringUtils.hasText(filter.getText())) {
            criteria.add(Restrictions.ilike("company.name", filter.getText(), MatchMode.ANYWHERE));
        }
        if (filter.getEmployeeNumberCategoryId() != null) {
            criteria.add(Restrictions.eq("company.employeeNumberCategory.id", filter.getEmployeeNumberCategoryId()));
        }
        return criteria;
    }

}
