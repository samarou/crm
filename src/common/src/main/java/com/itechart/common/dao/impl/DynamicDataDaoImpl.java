package com.itechart.common.dao.impl;

import com.itechart.common.dao.DynamicDataDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.common.model.persistent.BaseEntity;

import java.io.Serializable;

public abstract class DynamicDataDaoImpl<T extends BaseEntity, I extends Serializable, F extends PagingFilter>
        extends BaseHibernateDao<T, I, F> implements DynamicDataDao<T, I> {

    @SuppressWarnings("unchecked")
    public I save(T object) {
        return (I) getHibernateTemplate().save(object);
    }

    @Override
    public void update(T object) {
        getHibernateTemplate().update(object);
    }

    @Override
    public T merge(T object) {
        return getHibernateTemplate().merge(object);
    }

    @Override
    public void delete(T object) {
        getHibernateTemplate().delete(object);
    }

    @Override
    public void delete(I id) {
        T entity = get(id);
        if (entity != null) {
            delete(entity);
        }
    }

}
