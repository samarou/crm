package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.service.dao.DictionaryDao;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

/**
 * @author andrei.samarou
 */
@Repository
public class DictionaryDaoImpl extends AbstractHibernateDao implements DictionaryDao {

    @Override
    public ObjectType getObjectType(Long id) {
        ObjectType type = getHibernateTemplate().get(ObjectType.class, id);
        if (type == null) {
            throw new DataRetrievalFailureException("ObjectType wasn't found: " + id);
        }
        return type;
    }

    @Override
    public ObjectType getObjectTypeByName(String name) {
        ObjectType type = findObject("from ObjectType ot where ot.name = ?", name);
        if (type == null) {
            throw new DataRetrievalFailureException("ObjectType wasn't found by name: " + name);
        }
        return type;
    }
}
