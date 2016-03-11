package com.itechart.security.business.dao;


import com.itechart.security.business.model.persistent.BaseEntity;

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

    void delete(T object);

    void deleteById(Long id);

    List<T> findByIds(List<? extends Serializable> ids);

    int count();

    String getIdPropertyName();
}
