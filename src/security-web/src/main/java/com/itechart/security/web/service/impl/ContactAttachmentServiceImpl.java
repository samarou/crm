package com.itechart.security.web.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.security.business.model.dto.AttachmentDto;
import com.itechart.security.business.service.AttachmentService;
import com.itechart.security.web.service.ContactAttachmentService;
import com.itechart.security.web.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    public void save(Long contactId, MultipartFile file, String attachmentDto) {
        if (!file.isEmpty()) {
            try {
                Long attachmentId = saveDetails(attachmentDto, contactId);
                saveFile(contactId, file.getInputStream(), attachmentId);
            } catch (IOException ex) {
                logger.error("can't save attachment", ex);
                throw new RuntimeException("can't save attachment", ex);
            }
        } else {
            logger.warn("attachment wasn't added, because file is not present");
            throw new RuntimeException("Expected a file, but no file was found.");
        }
    }

    private Long saveDetails(String attachmentDtoString, Long contactId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AttachmentDto attachmentDto = mapper.readValue(attachmentDtoString, AttachmentDto.class);
        attachmentDto.setContactId(contactId);
        attachmentDto.setDateUpload(new Date());
        return attachmentService.insertAttachment(attachmentDto);
    }

    private void saveFile(Long contactId, InputStream fileInputStream, Long attachmentId) {
        try {
            FileUtil.saveFile(fileInputStream, FileUtil.getAttachmentPath(contactId, attachmentId));
        } catch (IOException ex) {
            logger.error("file upload failed, deleting attachment info from database", ex);
            if (attachmentId != 0) {
                attachmentService.deleteById(attachmentId);
            }
            throw new RuntimeException("file upload failed", ex);
        }
    }


}
