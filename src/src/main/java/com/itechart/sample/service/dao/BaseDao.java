package com.itechart.sample.service.dao;

import com.itechart.sample.model.persistent.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author andrei.samarou
 */
public interface BaseDao<T extends BaseEntity> {

    T get(Serializable id);

    List<T> loadAll();

    Serializable save(T object);

    void saveOrUpdate(T object);

    void update(T object);

    T merge(T object);
}
