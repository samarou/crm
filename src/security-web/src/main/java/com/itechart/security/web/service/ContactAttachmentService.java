package com.itechart.security.web.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ContactAttachmentService {

    public void download(Long contactId, Long attachmentId, HttpServletResponse response);

    public void save(Long contactId, MultipartFile file, String attachmentDto);
}
