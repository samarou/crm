package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.DynamicDataDaoImpl;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.dao.AttachmentDao;
import com.itechart.security.business.model.persistent.Attachment;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class AttachmentDaoImpl extends DynamicDataDaoImpl<Attachment, Long, PagingFilter> implements AttachmentDao {

    @Override
    public void delete(Long id) {
        Attachment attachment = getHibernateTemplate().get(Attachment.class, id);
        if (attachment != null) {
            attachment.setDateDeleted(new Date());
            update(attachment);
        }
    }

    @Override
    public List<Attachment> getAttachments(Long contactId) {
        String query = "from Attachment a where a.contact.id = ? and a.dateDeleted is null";
        return find(query, contactId);
    }
}
