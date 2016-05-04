package com.itechart.security.web.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itechart.security.business.model.persistent.Attachment;
import com.itechart.security.business.service.AttachmentService;
import com.itechart.security.web.model.dto.AttachmentDto;
import com.itechart.security.web.service.ContactAttachmentService;
import org.apache.commons.io.FileUtils;
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

    private static File uploadDir;

    @Autowired
    private AttachmentService attachmentService;

    @Override
    public void download(Long contactId, Long attachmentId, HttpServletResponse response) {
        if (attachmentId != 0L) {
            Attachment attachment = attachmentService.get(attachmentId);
            setHeaders(response, attachment.getName());
            try {
                File file = getFile(contactId, attachmentId);
                if (file != null) {
                    BufferedInputStream inputStream = null;
                    ServletOutputStream outputStream = null;
                    String mimeType = Files.probeContentType(file.toPath());
                    response.setContentType(mimeType);
                    try {
                        inputStream = new BufferedInputStream(new FileInputStream(file));
                        outputStream = response.getOutputStream();
                        FileCopyUtils.copy(inputStream, outputStream);
                        response.flushBuffer();
                    } finally {
                        closeStream(inputStream);
                        closeStream(outputStream);
                    }
                } else {
                    logger.error("can't find attachment {} with id {} for contact {} in {}", attachment.getName(), attachmentId, contactId, uploadDir);
                    throw new RuntimeException("file is not on server");
                }
            } catch (IOException ex) {
                logger.error("can't download attachment {} with id {} for contact {} in {}", attachment.getName(), attachmentId, contactId, uploadDir, ex);
                throw new RuntimeException("error happened during file download");
            }
        }
    }

    private void setHeaders(HttpServletResponse response, String attachmentName) {
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Content-Disposition", "inline;filename*=UTF-8''" + attachmentName + ";");
    }

    @Override
    public void save(Long contactId, MultipartFile file, String attachmentDto) {
        if (!file.isEmpty()) {
            try {
                Long attachmentId = saveDetails(attachmentDto, contactId);
                saveFile(contactId, file, attachmentId);
            } catch (IOException ex) {
                logger.error("can't save attachment", ex);
                throw new RuntimeException("can't save attachment");
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
        Attachment att = convert(attachmentDto);
        att.setDateUpload(new Date());
        return attachmentService.insertAttachment(att);
    }

    private void saveFile(Long contactId, MultipartFile file, Long attachmentId) {
        try {
            BufferedOutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                File filePath = getPathToFile(contactId, attachmentId, "." + FilenameUtils.getExtension(file.getOriginalFilename()));
                inputStream = file.getInputStream();
                outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                FileCopyUtils.copy(inputStream, outputStream);
            } finally {
                closeStream(inputStream);
                closeStream(outputStream);
            }
        } catch (IOException ex) {
            logger.error("file upload failed, deleting attachment info from database", ex);
            if (attachmentId != 0) {
                attachmentService.deleteById(attachmentId);
            }
            throw new RuntimeException("file upload failed");
        }
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = new File(uploadDir);
        if (ContactAttachmentServiceImpl.uploadDir.mkdirs()) {
            logger.debug("directory created: {}", ContactAttachmentServiceImpl.uploadDir);
        } else {
            logger.debug("can't create directory {}", this.uploadDir);
        }
    }

    private File getPathToFile(Long contactId, Long attachmentId, String extension) {
        File contactFilesDir = new File(uploadDir, File.separator + contactId);
        return new File(contactFilesDir, File.separator + attachmentId + extension);
    }

    private File getFile(Long contactId, Long attachmentId) {
        File dir  = FileUtils.getFile(uploadDir, "" + contactId, "" + attachmentId);
        File[] listOfFiles = dir.listFiles();
        if (listOfFiles != null) {
            for (File f : listOfFiles) {
                if (f.getName().startsWith("" + attachmentId)) {
                    return f;
                }
            }
        }
        return null;
    }

    private <T extends Closeable> void closeStream(T stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                logger.error("can't close stream", e);
            }
        }
    }
}
