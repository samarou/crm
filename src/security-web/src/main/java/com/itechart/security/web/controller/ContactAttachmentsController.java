package com.itechart.security.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.security.business.dao.impl.AttachmentDaoImpl;
import com.itechart.security.business.model.persistent.Attachment;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.service.AttachmentService;
import com.itechart.security.business.service.ContactService;
import com.itechart.security.web.model.dto.AttachmentDto;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static com.itechart.security.web.model.dto.Converter.convert;
import static com.itechart.security.web.model.dto.Converter.convertAttachments;

@RestController
public class ContactAttachmentsController {

    private static final Logger logger = LoggerFactory.getLogger(ContactAttachmentsController.class);

    private static String uploadHome;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ContactService contactService;

/*    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.POST)
    public void addAttachments(@PathVariable Long contactId, @RequestBody List<AttachmentDto> attachments) {
        logger.debug("adding attachments for contact: {}, number of attachments: {}", contactId, attachments.size());
        Contact contact = contactService.get(contactId);
        for (AttachmentDto dto : attachments) {
            Attachment attachment = convert(dto);
            attachment.setContact(contact);
            attachment.setUploadDate(new Date());
            attachmentService.saveAttachment(attachment);
        }
    }*/

    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.PUT)
    public void updateAttachments(@PathVariable Long contactId, @RequestBody List<AttachmentDto> attachments) {
        logger.debug("update attachments for contact: {}, number of attachments: {}", contactId, attachments.size());
        Contact contact = contactService.get(contactId);
        for (AttachmentDto dto : attachments) {
            Attachment attachment = convert(dto);
            attachment.setContact(contact);
            attachmentService.updateAttachment(attachment);
        }
    }

    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.GET)
    public List<AttachmentDto> getAttachments(@PathVariable Long contactId) {
        logger.debug("getting attachments for contact {}", contactId);
        List<Attachment> attachments = attachmentService.getAttachments(contactId);
        return convertAttachments(attachments);
    }

  /*  @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.POST)
    public void addAttachments(@PathVariable Long contactId, @RequestParam("file") MultipartFile file, @RequestParam("attachment") AttachmentDto attachment) throws IOException {
        logger.debug("receiving files {} from contact {}", file.getOriginalFilename(), attachment.getContactId());

    }*/


    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void uploadFile(@PathVariable Long contactId, @RequestParam("file") MultipartFile file, @RequestParam("attachment") String attachment) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AttachmentDto dto = mapper.readValue(attachment,AttachmentDto.class);
        logger.debug("receiving file {} from contact {}, attachment {}", file.getOriginalFilename(), contactId, attachment);
        if (!file.isEmpty()) {
            Long attachmentId = 0L;
            try {
                Contact contact = contactService.get(contactId);
                Attachment att = convert(dto);
                att.setContact(contact);
                att.setDateUpload(new Date());
                attachmentId = attachmentService.saveAttachment(att);
                String savingPath = getPathToFile(contactId, attachmentId, "."+ FilenameUtils.getExtension(file.getOriginalFilename()));
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(new File(savingPath)));
                FileCopyUtils.copy(file.getInputStream(), stream);
                stream.close();
                logger.debug("your file was successfully saved under path: {}", savingPath);
            } catch (Exception e) {
                logger.error("file upload failed, deleting attachment info from database", e);
                if (attachmentId != 0) {
                    attachmentService.deleteById(attachmentId);
                }
            }
        } else {
            logger.warn("file wasn't uploaded, because it's body is empty");
        }
    }

    private String getPathToFile(Long contactId, Long attachmentId, String extension) {
        File f = new File(this.uploadHome + File.separator + contactId);
        f.mkdirs();
        return f.getPath()+ File.separator + attachmentId + extension;
    }

    @RequestMapping(value = "/contacts/{contactId}/attachments/{attachmentId}", method = RequestMethod.DELETE)
    public void removeAttachment(@PathVariable Long contactId, @PathVariable Long attachmentId) {
        logger.debug("removing attachment for contact {}, attachment id {}", contactId, attachmentId);
        attachmentService.deleteById(attachmentId);
    }

    public void setUploadHome(String uploadHome) {
        this.uploadHome = uploadHome;
        File dir = new File(this.uploadHome);
        if (!this.uploadHome.isEmpty()) {
            if (dir.mkdirs()) {
                logger.debug("directory created: {}", dir);
            } else {
                logger.debug("can't create directory {}", this.uploadHome);
            }
        }
    }
}