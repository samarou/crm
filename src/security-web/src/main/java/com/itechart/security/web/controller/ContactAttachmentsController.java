package com.itechart.security.web.controller;

import com.itechart.security.business.model.persistent.Attachment;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.service.AttachmentService;
import com.itechart.security.business.service.ContactService;
import com.itechart.security.web.model.dto.AttachmentDto;
import com.itechart.security.web.service.ContactAttachmentService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static com.itechart.security.web.model.dto.Converter.convert;
import static com.itechart.security.web.model.dto.Converter.convertAttachments;

@RestController
public class ContactAttachmentsController {

    private static final Logger logger = LoggerFactory.getLogger(ContactAttachmentsController.class);

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ContactAttachmentService contactAttachmentService;

    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.PUT)
    public void updateAttachment(@PathVariable Long contactId, @RequestBody AttachmentDto attachmentDto) {
        logger.debug("update attachments for contact: {}, attachment: {}", contactId, attachmentDto.getId());
        attachmentService.updateAttachment(convert(attachmentDto));
    }

    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.GET)
    public List<AttachmentDto> getAttachments(@PathVariable Long contactId) {
        logger.debug("getting attachments for contact {}", contactId);
        List<Attachment> attachments = attachmentService.getAttachments(contactId);
        return convertAttachments(attachments);
    }

    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addAttachment(@PathVariable Long contactId, @RequestParam("file") MultipartFile file, @RequestParam("attachment") String attachment) {
        logger.debug("uploading file {} from contact {}, attachment {}", file.getOriginalFilename(), contactId, attachment);
        contactAttachmentService.saveAttachment(contactId, file, attachment);
    }

    @RequestMapping(value = "/files/contacts/{contactId}/attachments/{attachmentId}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable Long contactId, @PathVariable Long attachmentId, HttpServletResponse response) {
        logger.debug("downloading attachment {} from contact {}", attachmentId, contactId);
        contactAttachmentService.addAttachmentToResponse(contactId, attachmentId, response);
    }


    @RequestMapping(value = "/contacts/{contactId}/attachments/{attachmentId}", method = RequestMethod.DELETE)
    public void removeAttachment(@PathVariable Long contactId, @PathVariable Long attachmentId) {
        logger.debug("removing attachment for contact {}, attachment id {}", contactId, attachmentId);
        attachmentService.deleteById(attachmentId);
    }


}