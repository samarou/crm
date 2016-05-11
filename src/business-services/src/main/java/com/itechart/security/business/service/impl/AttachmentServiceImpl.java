package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.AttachmentDao;
import com.itechart.security.business.model.dto.AttachmentDto;
import com.itechart.security.business.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.itechart.security.business.model.dto.DtoConverter.convert;
import static com.itechart.security.business.model.dto.DtoConverter.convertAttachments;

public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentDao attachmentDao;

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentDto> loadAll() {
        return convertAttachments(attachmentDao.loadAll());
    }

    @Override
    @Transactional
    public Long insertAttachment(AttachmentDto attachmentDto) {
        return attachmentDao.save(convert(attachmentDto));
    }

    @Override
    @Transactional(readOnly = true)
    public AttachmentDto get(Long id) {
        return convert(attachmentDao.get(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentDto> getAttachments(Long contactId) {
        return convertAttachments(attachmentDao.getAttachments(contactId));
    }

    @Override
    @Transactional
    public void updateAttachment(AttachmentDto attachmentDto) {
        attachmentDao.update(convert(attachmentDto));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        attachmentDao.deleteById(id);
    }
}
