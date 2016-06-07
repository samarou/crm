package com.itechart.security.business.dao;

import com.itechart.common.dao.DynamicDataDao;
import com.itechart.security.business.model.persistent.Attachment;
import java.util.List;

public interface AttachmentDao extends DynamicDataDao<Attachment, Long> {

    Attachment get(Long id);

    List<Attachment> getAttachments(Long contactId);
}
