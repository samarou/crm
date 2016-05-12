package com.itechart.security.web.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ContactAttachmentService {

    public File getFile(Long contactId, Long attachmentId);

    public void save(Long contactId, MultipartFile file, String attachmentDto);

    public String getNameOfAttachment(Long attachmentId);
}
