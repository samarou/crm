package com.itechart.security.web.controller;

import com.itechart.security.business.model.dto.ContactDto;

import com.itechart.security.business.service.ContactService;
import com.itechart.security.business.service.FileService;
import com.itechart.security.web.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/images/contact/{contactId}", method = GET)
    public void downloadAttachment(@PathVariable Long contactId,
                                   HttpServletResponse response) {
        logger.debug("downloading image for contact {}", contactId);
        FileUtil.fixSSLHandshakeException();
        response.setHeader("Cache-Control", "no-cache");
        ContactDto contact = contactService.get(contactId);
        try {
            URL file = new URL(contact.getPhotoUrl());
            FileUtil.copyImageToResponse(file, response);
        } catch (IOException ex) {
            logger.error("can't download attachment with id for contact {} ", contactId);
            throw new RuntimeException("image wasn't loaded");
        }
    }
}
