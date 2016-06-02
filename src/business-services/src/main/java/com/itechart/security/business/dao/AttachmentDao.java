package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.Attachment;

import java.util.List;

public interface AttachmentDao {
    Long save(Attachment attachment);

    List<Attachment> loadAll();

    void update(Attachment attachment);

    Attachment get(Long id);

    void delete(Long id);

    List<Attachment> getAttachments(Long contactId);
}
