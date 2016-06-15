package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.HistoryEntryDto;
import com.itechart.security.business.model.persistent.ObjectKey;

/**
 * @author yauheni.putsykovich
 */
public interface HistoryEntryService {

    HistoryEntryDto getLastModification(ObjectKey objectKey);

    void startHistory(ObjectKey objectKey);

    void updateHistory(ObjectKey objectKey);

}
