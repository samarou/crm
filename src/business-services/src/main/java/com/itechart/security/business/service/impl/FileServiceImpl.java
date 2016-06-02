package com.itechart.security.business.service.impl;

import com.itechart.security.business.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private String uploadDir;

    @Override
    public void moveFileToContactDirectory(String tempFilePath, Long contactId, Long attachmentId) throws IOException {
        Path attachmentPath = getAttachment(contactId, attachmentId).toPath();
        Path tempFile = new File(tempFilePath).toPath();
        Files.move(tempFile, attachmentPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public File getAttachment(Long contactId, Long attachmentId) {
        return new File(getContactDirectory(contactId), attachmentId.toString());
    }

    private File getContactDirectory(Long contactId) {
        File contactFilesDir = new File(getContactsDirectory(), contactId.toString());
        createDirectoryIfNotExists(contactFilesDir);
        return contactFilesDir;
    }

    private File getContactsDirectory() {
        File contactsDir = new File(uploadDir, "contacts");
        createDirectoryIfNotExists(contactsDir);
        return contactsDir;
    }

    private void createDirectoryIfNotExists(File directory) {
        if (!directory.exists()) {
            createDirectory(directory);
        }
    }

    private void createDirectory(File directory) {
        if (directory.mkdirs()) {
            logger.debug("directory created: {}", directory);
        } else {
            if (!directory.exists()) {
                logger.debug("can't create directory: {}", directory);
                throw new RuntimeException("problem with file system on server occurred");
            }
        }
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
        createDirectoryIfNotExists(new File(uploadDir));
    }
}
