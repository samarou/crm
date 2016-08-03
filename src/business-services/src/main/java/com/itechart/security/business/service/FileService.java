package com.itechart.security.business.service;

import com.itechart.security.business.model.persistent.Contact;

import java.io.File;
import java.io.IOException;

public interface FileService {
    void moveFileToContactDirectory(String tempFilePath, Long contactId, Long attachmentId) throws IOException;

    void moveImageToContactDirectory(Contact contact) throws IOException;

    File getAttachment(Long contactId, Long attachmentId);

    void setUploadDir(String uploadDir);
}
