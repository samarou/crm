package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.HistoryEntryDto;
import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.model.persistent.acl.AclObjectKey;

/**
 * @author yauheni.putsykovich
 */
public interface HistoryEntryService {

    void saveOrUpdate(HistoryEntry historyEntry);

    HistoryEntryDto getLastModification(AclObjectKey objectKey);

    void startHistory(long contactId);

    void updateHistory(long contactId);

}
