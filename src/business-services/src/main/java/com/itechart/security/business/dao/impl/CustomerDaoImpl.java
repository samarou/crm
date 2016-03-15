package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.CustomerDao;
import com.itechart.security.business.filter.CustomerFilter;
import com.itechart.security.business.model.persistent.Customer;
import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class CustomerDaoImpl extends AbstractHibernateDao<Customer> implements CustomerDao {

    @Override
    public Long save(Customer customer) {
        return (Long) getHibernateTemplate().save(customer);
    }

    @Override
    @AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.READ}))
    public List<Customer> loadAll() {
        return getHibernateTemplate().loadAll(Customer.class);
    }

    @Override
    public void update(Customer customer) {
        getHibernateTemplate().update(customer);
    }

    @Override
    public void deleteById(Long id) {
        Customer customer = getHibernateTemplate().get(Customer.class, id);
        if (customer != null) getHibernateTemplate().delete(customer);
    }

    @Override
    @AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.READ}))
    public int countCustomers(CustomerFilter filter){
        return getHibernateTemplate().executeWithNativeSession(session -> {
            Criteria criteria = createFilterCriteria(session, filter);
            criteria.setProjection(Projections.rowCount());
            return ((Number) criteria.uniqueResult()).intValue();
        });
    }

    @Override
    @AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.READ}))
    public List<Customer> findCustomers(CustomerFilter filter) {
        return getHibernateTemplate().executeWithNativeSession(session -> {
            Criteria criteria = createFilterCriteria(session, filter);
            return executePagingDistinctCriteria(session, criteria, filter);
        });
    }

    private Criteria createFilterCriteria(Session session, CustomerFilter filter) {
        Criteria criteria = session.createCriteria(Customer.class, "u");
        if (StringUtils.hasText(filter.getText())) {
            criteria.add(Restrictions.disjunction(
                    Restrictions.ilike("u.address", filter.getText(), MatchMode.ANYWHERE),
                    Restrictions.ilike("u.firstName", filter.getText(), MatchMode.ANYWHERE),
                    Restrictions.ilike("u.lastName", filter.getText(), MatchMode.ANYWHERE),
                    Restrictions.ilike("u.email", filter.getText(), MatchMode.ANYWHERE)
            ));
        }
        return criteria;
    }
}