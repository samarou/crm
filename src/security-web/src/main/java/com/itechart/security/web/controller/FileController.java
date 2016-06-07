package com.itechart.security.web.controller;

import com.itechart.security.business.service.FileService;
import com.itechart.security.web.util.TempFileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import static com.itechart.security.web.util.TempFileUtil.closeStream;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/files/contacts/{contactId}/attachments/{attachmentId}/{fileName}", method = GET)
    public void downloadAttachment(@PathVariable Long contactId, @PathVariable Long attachmentId, @PathVariable String fileName, HttpServletResponse response) {
        logger.debug("downloading attachment {} from contact {}", attachmentId, contactId);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Content-Disposition", "inline;filename*=UTF-8''" + fileName + ";");
        try {
            String mimeType = Files.probeContentType(new File(fileName).toPath());
            response.setContentType(mimeType);
            File file = fileService.getAttachment(contactId, attachmentId);
            copyFileToResponse(file, response);
        } catch (IOException ex) {
            logger.error("can't download attachment with id {} for contact {} in {}", attachmentId, contactId, ex);
            throw new RuntimeException("error happened during file download", ex);
        }
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

    @RequestMapping(value = "/files", method = POST)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return TempFileUtil.saveTempFile(file.getInputStream());
        } catch (IOException e) {
            logger.error("can't upload file to server", e);
            throw new RuntimeException("file upload failed", e);
        }
    }
}
