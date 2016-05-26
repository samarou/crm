package com.itechart.security.business.dao;

import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.model.persistent.acl.AclObjectKey;

/**
 * @author yauheni.putsykovich
 */
public interface HistoryEntryDao extends BaseDao<HistoryEntry> {

    HistoryEntry getLastModification(AclObjectKey objectKey);

}
