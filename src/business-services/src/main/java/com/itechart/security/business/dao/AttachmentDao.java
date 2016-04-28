package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.Attachment;
import java.util.List;
import java.util.Set;

public interface AttachmentDao {
    Long save(Attachment attachment);

    List<Attachment> loadAll();

    void update(Attachment attachment);

    Attachment get(Long id);

    void deleteById(Long id);

    List<Attachment> getAttachments(Long contactId);
}
