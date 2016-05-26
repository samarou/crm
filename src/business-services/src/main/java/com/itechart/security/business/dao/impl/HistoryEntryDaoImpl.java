package com.itechart.security.business.dao.impl;

import com.itechart.security.business.dao.HistoryEntryDao;
import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.business.model.persistent.ObjectKey;
import org.springframework.stereotype.Repository;

import static org.hibernate.criterion.Restrictions.eq;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class HistoryEntryDaoImpl extends BaseHibernateDao<HistoryEntry> implements HistoryEntryDao {

    @Override
    public HistoryEntry getLastModification(ObjectKey objectKey) {
        return getHibernateTemplate().execute(session ->
                (HistoryEntry) session.createCriteria(HistoryEntry.class, "h")
                        .add(eq("h.objectKey", objectKey))
                        .uniqueResult());
    }

}
