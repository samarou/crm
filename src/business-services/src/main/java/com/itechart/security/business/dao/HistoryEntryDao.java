package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.business.model.persistent.ObjectKey;

/**
 * @author yauheni.putsykovich
 */
public interface HistoryEntryDao extends BaseDao<HistoryEntry> {

    HistoryEntry getLastModification(ObjectKey objectKey);

}
