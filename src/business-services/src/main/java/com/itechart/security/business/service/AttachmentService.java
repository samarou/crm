package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.AttachmentDto;

import java.util.List;

public interface AttachmentService {
    List<AttachmentDto> loadAll();

    Long insertAttachment(AttachmentDto attachment);

    AttachmentDto get(Long id);

    List<AttachmentDto> getAttachments(Long contactId);

    void updateAttachment(AttachmentDto attachmentDto);

    void deleteById(Long id);
}
