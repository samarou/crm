package com.itechart.security.service.impl;

import com.itechart.security.model.persistent.ObjectType;
import com.itechart.security.service.ObjectTypeService;
import com.itechart.security.dao.ObjectTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author andrei.samarou
 */
@Service
@CacheConfig(cacheManager = "mapBasedCacheManager")
public class ObjectTypeServiceImpl implements ObjectTypeService {

    @Autowired
    private ObjectTypeDao objectTypeDao;

    @Override
    public ObjectType getObjectType(Long id) {
        return objectTypeDao.getObjectType(id);
    }

    @Override
    @Cacheable("objectTypeByName")
    public ObjectType getObjectTypeByName(String name) {
        return objectTypeDao.getObjectTypeByName(name);
    }
}
