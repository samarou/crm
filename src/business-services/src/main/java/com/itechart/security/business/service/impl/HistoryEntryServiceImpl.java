package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.HistoryEntryDao;
import com.itechart.security.business.model.dto.HistoryEntryDto;
import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.business.service.HistoryEntryService;
import com.itechart.security.model.persistent.ObjectType;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.acl.AclObjectKey;
import com.itechart.security.service.ObjectTypeService;
import com.itechart.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

import static com.itechart.security.business.model.dto.DtoConverter.convert;
import static com.itechart.security.core.SecurityUtils.getAuthenticatedUserId;

/**
 * @author yauheni.putsykovich
 */
@Service
public class HistoryEntryServiceImpl implements HistoryEntryService {

    @Autowired
    private HistoryEntryDao historyDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectTypeService objectTypeService;

    @Override
    @Transactional
    public void saveOrUpdate(HistoryEntry historyEntry) {
        historyDao.saveOrUpdate(historyEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public HistoryEntryDto getLastModification(AclObjectKey objectKey) {
        return convert(historyDao.getLastModification(objectKey));
    }

    @Override
    @Transactional
    public void startHistory(long contactId) {
        AclObjectKey objectKey = getObjectIdentityId(contactId);
        User user = userService.getUser(getAuthenticatedUserId());
        Date now = Date.from(Instant.now());
        HistoryEntry historyEntry = new HistoryEntry(objectKey, user, now, user, now);
        saveOrUpdate(historyEntry);
    }

    @Override
    @Transactional
    public void updateHistory(long contactId) {
        AclObjectKey objectKey = getObjectIdentityId(contactId);
        HistoryEntry historyEntry = historyDao.getLastModification(objectKey);
        historyEntry.setModificationDate(Date.from(Instant.now()));
        historyEntry.setEditor(userService.getUser(getAuthenticatedUserId()));
        saveOrUpdate(historyEntry);
    }

    private AclObjectKey getObjectIdentityId(long contactId) {
        ObjectType objectType = objectTypeService.getObjectTypeByName(ObjectTypes.CONTACT.getName());
        return new AclObjectKey(objectType.getId(), contactId);
    }
}
