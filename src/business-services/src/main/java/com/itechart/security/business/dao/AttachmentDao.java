package com.itechart.security.business.dao;

import com.itechart.common.dao.BaseDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.security.business.model.persistent.Attachment;

import java.util.List;

public interface AttachmentDao extends BaseDao<Attachment, Long, PagingFilter> {

    Attachment get(Long id);

    List<Attachment> getAttachments(Long contactId);
}
