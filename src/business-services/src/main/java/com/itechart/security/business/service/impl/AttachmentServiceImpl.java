package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.AttachmentDao;
import com.itechart.security.business.model.persistent.Attachment;
import com.itechart.security.business.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentDao attachmentDao;

    @Override
    @Transactional
    public List<Attachment> loadAll() {
        return attachmentDao.loadAll();
    }

    @Override
    @Transactional
    public Long insertAttachment(Attachment attachment) {
        return attachmentDao.save(attachment);
    }

    @Override
    @Transactional
    public Attachment get(Long id) {
        return attachmentDao.get(id);
    }

    @Override
    @Transactional
    public List<Attachment> getAttachments(Long contactId) {
        return attachmentDao.getAttachments(contactId);
    }

    @Override
    @Transactional
    public void updateAttachment(Attachment attachment) {
        attachmentDao.update(attachment);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        attachmentDao.deleteById(id);
    }
}
