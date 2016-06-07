package com.itechart.common.dao;

import com.itechart.common.model.persistent.BaseEntity;

import java.util.List;

/**
 * Created by Alexander.Tolstoy on 06/03/2016.
 */
public interface StaticDataDao<T extends BaseEntity> {
    List<T> loadAll();
}
