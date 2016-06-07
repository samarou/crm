package com.itechart.common.dao;

import com.itechart.common.model.persistent.BaseEntity;

import java.io.Serializable;

public interface DynamicDataDao<T extends BaseEntity, I extends Serializable> {

    I save(T entity);

    void update(T entity);

    T merge(T entity);

    void delete(T entity);

    void delete(I id);

}
