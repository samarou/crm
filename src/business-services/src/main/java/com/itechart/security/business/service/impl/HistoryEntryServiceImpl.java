package com.itechart.security.business.service.impl;

import com.itechart.security.business.dao.HistoryEntryDao;
import com.itechart.security.business.model.dto.HistoryEntryDto;
import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.business.model.persistent.ObjectKey;
import com.itechart.security.business.service.HistoryEntryService;
import com.itechart.security.model.persistent.dto.PublicUserDto;
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

    @Override
    @Transactional
    public void saveOrUpdate(HistoryEntry historyEntry) {
        historyDao.saveOrUpdate(historyEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public HistoryEntryDto getLastModification(ObjectKey objectKey) {
        HistoryEntry historyEntry = historyDao.getLastModification(objectKey);
        PublicUserDto creator = userService.getPublicUser(historyEntry.getCreatorId());
        PublicUserDto editor = userService.getPublicUser(historyEntry.getEditorId());
        return convert(historyEntry, creator, editor);
    }

    @Override
    @Transactional
    public void startHistory(ObjectKey objectKey) {
        long userId = getAuthenticatedUserId();
        Date now = Date.from(Instant.now());
        HistoryEntry historyEntry = new HistoryEntry(objectKey, userId, now, userId, now);
        saveOrUpdate(historyEntry);
    }

    @Override
    @Transactional
    public void updateHistory(ObjectKey objectKey) {
        HistoryEntry historyEntry = historyDao.getLastModification(objectKey);
        historyEntry.setModificationDate(Date.from(Instant.now()));
        historyEntry.setEditorId(getAuthenticatedUserId());
        saveOrUpdate(historyEntry);
    }
}
