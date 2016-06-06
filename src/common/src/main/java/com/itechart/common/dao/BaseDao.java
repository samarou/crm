package com.itechart.common.dao;

import com.itechart.common.model.filter.PagingFilter;
import com.itechart.common.model.persistent.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author andrei.samarou
 */
public interface BaseDao<T extends BaseEntity, I extends Serializable, F extends PagingFilter> {

    T get(I id);

    List<T> find(F filter);

    int count(F filter);

    List<T> findByIds(List<I> ids);
}
