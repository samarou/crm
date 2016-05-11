package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.AttachmentDao;
import com.itechart.security.business.model.persistent.Attachment;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public class AttachmentDaoImpl extends AbstractHibernateDao<Attachment> implements AttachmentDao {
    @Override
    public Long save(Attachment attachment) {
        return (Long) getHibernateTemplate().save(attachment);
    }

    @Override
    public List<Attachment> loadAll() {
        return getHibernateTemplate().loadAll(Attachment.class);
    }

    @Override
    public void update(Attachment attachment) {
        getHibernateTemplate().update(attachment);
    }

    @Override
    public Attachment get(Long id) {
        return getHibernateTemplate().get(Attachment.class, id);
    }

    @Override
    public void deleteById(Long id) {
        Attachment attachment = getHibernateTemplate().get(Attachment.class, id);
        if (attachment != null) {
            attachment.setDateDeleted(new Date());
            getHibernateTemplate().update(attachment);
        }
    }

    @Override
    public List<Attachment> getAttachments(Long contactId) {
        String query = "from Attachment a where a.contact.id = :contactId and a.dateDeleted is null";
        return (List<Attachment>) getHibernateTemplate().findByNamedParam(query, "contactId", contactId);
    }
}
