package com.itechart.security.web.service;

import com.itechart.security.business.model.dto.AttachmentDto;

import java.io.File;

public interface ContactAttachmentService {

    public File getFile(Long contactId, Long attachmentId);

    public String getNameOfAttachment(Long attachmentId);

    public void save(Long contactId, AttachmentDto attachmentDto, String filePath);
}
