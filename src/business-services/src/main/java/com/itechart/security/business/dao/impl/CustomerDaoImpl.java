package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.CustomerDao;
import com.itechart.security.business.filter.CustomerFilter;
import com.itechart.security.business.model.persistent.Customer;
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
public class CustomerDaoImpl extends BaseHibernateDao<Customer> implements CustomerDao {

    @Override
    public Long save(Customer customer) {
        return (Long) getHibernateTemplate().save(customer);
    }

    @Override
    //@AclFilter
    //@AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.WRITE}, inherit = true))
    //@AclFilter(@AclFilterRule(type = Customer.class, permissions = {Permission.WRITE}))
    public List<Customer> loadAll() {
        return getHibernateTemplate().loadAll(Customer.class);
    }

    @Override
    public void update(Customer customer) {
        getHibernateTemplate().update(customer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Customer> findCustomers(CustomerFilter filter) {
        return getHibernateTemplate().execute(session -> {
            Criteria criteria = createFilterCriteria(filter, session);
            return executePagingDistinctCriteria(session, criteria, filter);
        });
    }

    @Override
    public int countCustomers(CustomerFilter filter) {
        return getHibernateTemplate().execute(session -> {
            Criteria criteria = createFilterCriteria(filter, session);
            appendSortingFilterConditions(criteria, filter);
            criteria.setProjection(Projections.rowCount());
//            return ((Number) criteria.uniqueResult()).intValue();
            return 0;
        });
    }

    private Criteria createFilterCriteria(CustomerFilter filter, Session session) {
        Criteria criteria = session.createCriteria(Customer.class, "c");
        if (StringUtils.hasText(filter.getText())) {
            criteria.add(Restrictions.disjunction(
                    Restrictions.ilike("c.firstName", filter.getText(), MatchMode.ANYWHERE),
                    Restrictions.ilike("c.lastName", filter.getText(), MatchMode.ANYWHERE),
                    Restrictions.ilike("c.email", filter.getText(), MatchMode.ANYWHERE),
                    Restrictions.ilike("c.address", filter.getText(), MatchMode.ANYWHERE)
            ));
        }
        return criteria;
    }
}