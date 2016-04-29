package com.itechart.security.business.service;

import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.persistent.Attachment;
import com.itechart.security.business.model.persistent.Contact;

import java.util.List;
import java.util.Set;

public interface AttachmentService {
    List<Attachment> loadAll();

    Long insertAttachment(Attachment attachment);

    Attachment get(Long id);

    List<Attachment> getAttachments(Long contactId);

    void updateAttachment(Attachment attachment);

    void deleteById(Long id);
}
