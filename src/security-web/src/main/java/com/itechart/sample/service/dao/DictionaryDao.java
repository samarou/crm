package com.itechart.sample.service.dao;

import com.itechart.sample.model.persistent.security.ObjectType;

/**
 * @author andrei.samarou
 */
public interface DictionaryDao {

    ObjectType getObjectType(Long id);

    ObjectType getObjectTypeByName(String name);
}
