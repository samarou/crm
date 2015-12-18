package com.itechart.security.dao;

import com.itechart.security.model.persistent.ObjectType;

/**
 * @author andrei.samarou
 */
public interface ObjectTypeDao {

    ObjectType getObjectType(Long id);

    ObjectType getObjectTypeByName(String name);
}
