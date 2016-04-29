package com.itechart.security.web.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;

import static com.itechart.security.web.model.dto.Converter.convert;


public class ContactAttachmentServiceImpl implements ContactAttachmentService {

    private static final Logger logger = LoggerFactory.getLogger(ContactAttachmentServiceImpl.class);

    private static String uploadHome;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ContactService contactService;

    @Override
    public void download(Long contactId, Long attachmentId, HttpServletResponse response) {
        if (attachmentId != 0L) {
            Attachment attachment = attachmentService.get(attachmentId);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Disposition", "inline;filename*=UTF-8''" + attachment.getName() + ";");
            logger.debug("filename is {}", attachment.getName());
            try {
                BufferedInputStream is = null;
                ServletOutputStream os = null;
                File file = getFile(contactId, attachmentId);
                if (file != null) {
                    String mimeType = Files.probeContentType(file.toPath());
                    response.setContentType(mimeType);
                    try {
                        is = new BufferedInputStream(new FileInputStream(file));
                        os = response.getOutputStream();
                        FileCopyUtils.copy(is, os);
                        response.flushBuffer();
                    } finally {
                        closeInputOutputStreams(is, os);
                    }
                } else {
                    logger.error("cannot find attachment {} with id {} for contact {} in {}", attachment.getName(), attachmentId, contactId, uploadHome);
                }
            } catch (FileNotFoundException e) {
                logger.error("cannot find attachment {} with id {} for contact {} in {}", attachment.getName(), attachmentId, contactId, uploadHome, e);
            } catch (IOException ex) {
                logger.error("cannot download attachment {} with id {} for contact {} in {}", attachment.getName(), attachmentId, contactId, uploadHome, ex);
            }
        }
    }

    @Override
    public void save(Long contactId, MultipartFile file, String attachmentDto) {
        if (!file.isEmpty()) {
            try {
                Long attachmentId = saveDetails(attachmentDto, contactId);
                saveFile(contactId, file, attachmentId);
            } catch (IOException ex){
                logger.error("can't save attachment",ex);
            }
        } else {
            logger.warn("attachment wasn't added, because file is not present");
        }
    }

    private Long saveDetails(String attachmentDtoString, Long contactId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AttachmentDto attachmentDto = mapper.readValue(attachmentDtoString, AttachmentDto.class);
        Attachment att = convert(attachmentDto);
        Contact contact = contactService.get(contactId);
        att.setContact(contact);
        att.setDateUpload(new Date());
        return attachmentService.insertAttachment(att);
    }

    private void saveFile(Long contactId, MultipartFile file, Long attachmentId) {
        BufferedOutputStream outputStream = null;
        InputStream inputStream = null;
        String savingPath = getPathToFile(contactId, attachmentId, "." + FilenameUtils.getExtension(file.getOriginalFilename()));
        try {
            try {
                inputStream = file.getInputStream();
                outputStream = new BufferedOutputStream(
                        new FileOutputStream(new File(savingPath)));
                FileCopyUtils.copy(inputStream, outputStream);
            } finally {
                closeInputOutputStreams(inputStream, outputStream);
            }
        } catch (IOException ex) {
            logger.error("file upload failed, deleting attachment info from database", ex);
            if (attachmentId != 0) {
                attachmentService.deleteById(attachmentId);
            }
        }
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

    private String getPathToFile(Long contactId, Long attachmentId, String extension) {
        File f = new File(uploadHome + File.separator + contactId);
        return f.getPath() + File.separator + attachmentId + extension;
    }

    private File getFile(Long contactId, Long attachmentId) {
        File dir = new File(uploadHome + File.separator + contactId);
        File[] listOfFiles = dir.listFiles();
        if (attachmentId != 0 && listOfFiles != null) {
            for (File f : listOfFiles) {
                if (f.getName().startsWith("" + attachmentId)) {
                    return f;
                }
            }
        }
        return null;
    }

    private <T extends Closeable> void closeStream(T stream){
        if(stream != null){
            try {
                stream.close();
            }catch (IOException e){
                logger.error("can't close stream", e);
            }
        }
    }
    private void closeInputOutputStreams(InputStream inputStream, OutputStream outputStream) {
        logger.debug("closing streams");
        closeStream(inputStream);
        closeStream(outputStream);
    }
}
