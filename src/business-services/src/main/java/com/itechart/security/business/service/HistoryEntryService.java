package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.HistoryEntryDto;
import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.business.model.persistent.ObjectKey;

/**
 * @author yauheni.putsykovich
 */
public interface HistoryEntryService {

    void saveOrUpdate(HistoryEntry historyEntry);

    HistoryEntryDto getLastModification(ObjectKey objectKey);

    void startHistory(ObjectKey objectKey);

    void updateHistory(ObjectKey objectKey);

}
