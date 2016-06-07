package com.itechart.common.dao.impl;

import com.itechart.common.model.filter.PagingFilter;
import com.itechart.common.model.persistent.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Basic DAO for entities that are not to be added/updated/deleted dynamically.
 */
public abstract class StaticDataDaoImpl<T extends BaseEntity, I extends Serializable, F extends PagingFilter>
        extends BaseHibernateDao<T, I, F>
        implements com.itechart.common.dao.StaticDataDao<T> {

    @Override
    public List<T> loadAll() {
        return getHibernateTemplate().loadAll(getPersistentClass());
    }

}
