package com.itechart.security.web.service.impl;

import com.itechart.security.business.model.dto.AttachmentDto;
import com.itechart.security.business.service.AttachmentService;
import com.itechart.security.web.service.ContactAttachmentService;
import com.itechart.security.web.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
public class ContactAttachmentServiceImpl implements ContactAttachmentService {

    private static final Logger logger = LoggerFactory.getLogger(ContactAttachmentServiceImpl.class);


    @Autowired
    private AttachmentService attachmentService;

    @Override
    public File getFile(Long contactId, Long attachmentId) {
        return FileUtil.getAttachment(contactId, attachmentId);
    }

    public String getNameOfAttachment(Long attachmentId) {
        AttachmentDto attachment = attachmentService.get(attachmentId);
        return attachment.getName();
    }

    @Override
    public void save(Long contactId, AttachmentDto attachmentDto, String filePath) {
        try {
            Long attachmentId = saveDetails(attachmentDto, contactId);
            moveFileToContactDirectory(filePath, contactId, attachmentId);
        } catch (IOException ex) {
            logger.error("can't save attachment", ex);
            throw new RuntimeException("can't save attachment", ex);
        }
    }

    private Long saveDetails(AttachmentDto attachmentDto, Long contactId) throws IOException {
        attachmentDto.setContactId(contactId);
        attachmentDto.setDateUpload(new Date());
        return attachmentService.insertAttachment(attachmentDto);
    }

    private void moveFileToContactDirectory(String tempFilePath, Long contactId, Long attachmentId) throws IOException {
        Path attachmentPath = FileUtil.getAttachmentPath(contactId, attachmentId).toPath();
        Path tempFile = new File(tempFilePath).toPath();
        Files.move(tempFile, attachmentPath, StandardCopyOption.REPLACE_EXISTING);
    }
}
