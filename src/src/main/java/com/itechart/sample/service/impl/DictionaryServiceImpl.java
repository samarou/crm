package com.itechart.sample.service.impl;

import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.service.DictionaryService;
import com.itechart.sample.service.dao.DictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author andrei.samarou
 */
@Service
@CacheConfig(cacheManager = "mapBasedCacheManager")
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryDao dictionaryDao;

    @Override
    public ObjectType getObjectType(Long id) {
        return dictionaryDao.getObjectType(id);
    }

    @Override
    @Cacheable("objectTypeByName")
    public ObjectType getObjectTypeByName(String name) {
        return dictionaryDao.getObjectTypeByName(name);
    }
}
