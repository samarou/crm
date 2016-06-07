package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.DynamicDataDaoImpl;
import com.itechart.security.business.dao.ContactDao;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.model.persistent.Skill;
import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public class ContactDaoImpl extends DynamicDataDaoImpl<Contact, Long, ContactFilter> implements ContactDao {

    @AclFilter(@AclFilterRule(type = Contact.class, permissions = {Permission.READ}))
    public Contact get(Long id) {
        return super.get(id);
    }

    @AclFilter(@AclFilterRule(type = Contact.class, permissions = {Permission.READ}))
    public List<Contact> find(ContactFilter filter) {
        return super.find(filter);
    }

    @AclFilter(@AclFilterRule(type = Contact.class, permissions = {Permission.READ}))
    public int count(ContactFilter filter) {
        return super.count(filter);
    }

    @Override
    public void delete(Long id) {
        Contact contact = getHibernateTemplate().get(Contact.class, id);
        if (contact != null) {
            contact.setDateDeleted(new Date());
            update(contact);
        }
    }

    @Override
    @AclFilter(@AclFilterRule(type = Contact.class, permissions = {Permission.READ}))
    public void deleteSkill(Long id) {
        Skill skill = getHibernateTemplate().get(Skill.class, id);
        if (skill != null) {
            skill.setDateDeleted(new Date());
            getHibernateTemplate().update(skill);
        }
    }

    @Override
    protected Criteria createFilterCriteria(Session session, ContactFilter filter) {
        Criteria criteria = session.createCriteria(Contact.class, "u");
        if (StringUtils.hasText(filter.getText())) {
            criteria.add(
                Restrictions.disjunction(
                    Restrictions.ilike("u.address.name", filter.getText(), MatchMode.ANYWHERE),
                    Restrictions.ilike("u.firstName", filter.getText(), MatchMode.ANYWHERE),
                    Restrictions.ilike("u.lastName", filter.getText(), MatchMode.ANYWHERE),
                    Restrictions.ilike("u.email.name", filter.getText(), MatchMode.ANYWHERE)
                )
            );
        }
        return criteria;
    }
}