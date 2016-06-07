package com.itechart.security.business.service;

import java.io.File;
import java.io.IOException;

public interface FileService {
    void moveFileToContactDirectory(String tempFilePath, Long contactId, Long attachmentId) throws IOException;

    File getAttachment(Long contactId, Long attachmentId);

    void setUploadDir(String uploadDir);
}
