package com.itechart.security.web.controller;

import com.itechart.security.business.model.dto.AttachmentDto;
import com.itechart.security.business.model.dto.helpers.AttachmentTempFileDto;
import com.itechart.security.business.service.AttachmentService;
import com.itechart.security.web.service.ContactAttachmentService;
import com.itechart.security.web.util.FileUtil;
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
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;

import static com.itechart.security.web.util.FileUtil.closeStream;

@RestController
public class ContactAttachmentsController {

    private static final Logger logger = LoggerFactory.getLogger(ContactAttachmentsController.class);

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ContactAttachmentService contactAttachmentService;

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file){
        try {
            return FileUtil.saveTempFile(file.getInputStream());
        } catch (IOException e) {
            logger.error("can't upload file to server", e);
            throw new RuntimeException("file upload failed", e);
        }
    }

    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.PUT)
    public void updateAttachment(@PathVariable Long contactId, @RequestBody AttachmentDto attachmentDto) {
        logger.debug("update attachments for contact: {}, attachment: {}", contactId, attachmentDto.getId());
        attachmentService.updateAttachment(attachmentDto);
    }

    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.GET)
    public List<AttachmentDto> getAttachments(@PathVariable Long contactId) {
        logger.debug("getting attachments for contact {}", contactId);
        List<AttachmentDto> attachments = attachmentService.getAttachments(contactId);
        return attachments;
    }

    @RequestMapping(value = "/contacts/{contactId}/attachments", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addAttachment(@PathVariable Long contactId,  @RequestBody AttachmentTempFileDto attachmentWithFilePath) {
        AttachmentDto attachment = attachmentWithFilePath.getAttachment();
        String filePath = attachmentWithFilePath.getFilePath();
        logger.debug("adding attachment {} for contact {}, file for which is saved to {}", attachment.getName(), contactId, filePath);
        contactAttachmentService.save(contactId, attachment, filePath);
    }

    @RequestMapping(value = "/files/contacts/{contactId}/attachments/{attachmentId}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable Long contactId, @PathVariable Long attachmentId, HttpServletResponse response) {
        logger.debug("downloading attachment {} from contact {}", attachmentId, contactId);
        String fileName = contactAttachmentService.getNameOfAttachment(attachmentId);
        response.setHeader("Cache-Control", "no-cache");
        try {
            response.setHeader("Content-Disposition", "inline;filename*=UTF-8''" + URLEncoder.encode(fileName, "utf-8").replaceAll("\\+", "%20") + ";");
        } catch (UnsupportedEncodingException e) {
            logger.error("error while getting encoded filename", e);
        }
        try {
            String mimeType = Files.probeContentType(new File(fileName).toPath());
            response.setContentType(mimeType);
            File file = contactAttachmentService.getFile(contactId, attachmentId);
            copyFileToResponse(file, response);
        } catch (IOException ex) {
            logger.error("can't download attachment with id {} for contact {} in {}", attachmentId, contactId, ex);
            throw new RuntimeException("error happened during file download", ex);
        }
    }

    @RequestMapping(value = "/files/contacts/{contactId}/attachments/{attachmentId}/check", method = RequestMethod.GET)
    public void checkFile(@PathVariable Long contactId, @PathVariable Long attachmentId) {
        logger.debug("checking attachment {} from contact {}", attachmentId, contactId);
        File file = contactAttachmentService.getFile(contactId, attachmentId);
        if (!file.exists()) {
            throw new RuntimeException("error happened during file download");
        }
    }

    @RequestMapping(value = "/contacts/{contactId}/attachments/{attachmentId}", method = RequestMethod.DELETE)
    public void removeAttachment(@PathVariable Long contactId, @PathVariable Long attachmentId) {
        logger.debug("removing attachment for contact {}, attachment id {}", contactId, attachmentId);
        attachmentService.deleteById(attachmentId);
    }

    private void copyFileToResponse(File file, HttpServletResponse response) throws IOException {
        BufferedInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            outputStream = response.getOutputStream();
            FileCopyUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } finally {
            closeStream(inputStream);
            closeStream(outputStream);
        }
    }


}