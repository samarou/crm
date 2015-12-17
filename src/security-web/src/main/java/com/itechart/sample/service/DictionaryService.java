package com.itechart.sample.service;

import com.itechart.sample.model.persistent.security.ObjectType;

/**
 * @author andrei.samarou
 */
public interface DictionaryService {

    ObjectType getObjectType(Long id);

    ObjectType getObjectTypeByName(String name);

}
