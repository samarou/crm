package com.itechart.security.business.dao.impl;

import com.itechart.common.dao.impl.AbstractHibernateDao;
import com.itechart.security.business.dao.HistoryEntryDao;
import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.business.model.persistent.ObjectKey;
import org.springframework.stereotype.Repository;

import static org.hibernate.criterion.Restrictions.eq;

/**
 * @author yauheni.putsykovich
 */
@Repository
public class HistoryEntryDaoImpl extends AbstractHibernateDao<HistoryEntry> implements HistoryEntryDao {

    @Override
    public HistoryEntry getLastModification(ObjectKey objectKey) {
        return getHibernateTemplate().execute(session ->
                (HistoryEntry) session.createCriteria(HistoryEntry.class, "h")
                        .add(eq("h.objectKey", objectKey))
                        .uniqueResult());
    }

    @Override
    public long save(HistoryEntry object) {
        return (Long) getHibernateTemplate().save(object);
    }

    @Override
    public void update(HistoryEntry object) {
        getHibernateTemplate().update(object);
    }

    @Override
    public void delete(ObjectKey objectKey) {
        HistoryEntry historyEntry = getLastModification(objectKey);
        getHibernateTemplate().delete(historyEntry);
    }

}
