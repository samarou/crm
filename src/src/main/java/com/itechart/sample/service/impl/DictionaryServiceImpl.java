package com.itechart.sample.service.impl;

import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.service.DictionaryService;
import com.itechart.sample.service.dao.DictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author andrei.samarou
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryDao dictionaryDao;

    @Override
    public ObjectType getObjectType(Long id) {
        //todo cache by id
        return dictionaryDao.getObjectType(id);
    }

    @Override
    public ObjectType getObjectTypeByName(String name) {
        //todo cache by name
        return dictionaryDao.getObjectTypeByName(name);
    }
}
