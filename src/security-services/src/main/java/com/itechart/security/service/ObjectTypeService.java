package com.itechart.security.service;

import com.itechart.security.model.persistent.ObjectType;

/**
 * @author andrei.samarou
 */
public interface ObjectTypeService {

    ObjectType getObjectType(Long id);

    ObjectType getObjectTypeByName(String name);

}
