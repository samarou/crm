package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.CustomerDao;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.persistent.Contact;
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
public class CustomerDaoImpl extends AbstractHibernateDao<Contact> implements CustomerDao {

    @Override
    public Long save(Contact contact) {
        return (Long) getHibernateTemplate().save(contact);
    }

    @Override
    @AclFilter(@AclFilterRule(type = Contact.class, permissions = {Permission.READ}))
    public List<Contact> loadAll() {
        return getHibernateTemplate().loadAll(Contact.class);
    }

    @Override
    public void update(Contact contact) {
        getHibernateTemplate().update(contact);
    }

    @Override
    public void deleteById(Long id) {
        Contact contact = getHibernateTemplate().get(Contact.class, id);
        if (contact != null) getHibernateTemplate().delete(contact);
    }

    @Override
    @AclFilter(@AclFilterRule(type = Contact.class, permissions = {Permission.READ}))
    public int countCustomers(ContactFilter filter){
        return getHibernateTemplate().executeWithNativeSession(session -> {
            Criteria criteria = createFilterCriteria(session, filter);
            criteria.setProjection(Projections.rowCount());
            return ((Number) criteria.uniqueResult()).intValue();
        });
    }

    @Override
    @AclFilter(@AclFilterRule(type = Contact.class, permissions = {Permission.READ}))
    public List<Contact> findCustomers(ContactFilter filter) {
        return getHibernateTemplate().executeWithNativeSession(session -> {
            Criteria criteria = createFilterCriteria(session, filter);
            return executePagingDistinctCriteria(session, criteria, filter);
        });
    }

    private Criteria createFilterCriteria(Session session, ContactFilter filter) {
        Criteria criteria = session.createCriteria(Contact.class, "u");
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