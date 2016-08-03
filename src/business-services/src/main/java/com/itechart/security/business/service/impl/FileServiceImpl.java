package com.itechart.security.business.service.impl;

import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    public void moveImageToContactDirectory(Contact contact) throws IOException{
        Path attachmentPath = new File(getContactDirectory(contact.getId()), "avatar").toPath();
        Path tempFile = new File(contact.getPhotoUrl()).toPath();
        Files.move(tempFile, attachmentPath, StandardCopyOption.REPLACE_EXISTING);
        String path = attachmentPath.toString();
        try {
            File file = new File(path);
            URL url = file.toURI().toURL();
            contact.setPhotoUrl(url.toString());
        }catch(Exception e){
            logger.error("can't move image to directory");
            throw new RuntimeException("can't upload image", e);
        }
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
            if (directory.mkdirs()) {
                logger.debug("directory created: {}", directory);
            } else {
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
