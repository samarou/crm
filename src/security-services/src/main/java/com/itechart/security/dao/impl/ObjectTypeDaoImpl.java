package com.itechart.security.dao.impl;

import com.itechart.security.model.persistent.ObjectType;
import com.itechart.security.dao.ObjectTypeDao;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

/**
 * @author andrei.samarou
 */
@Repository
public class ObjectTypeDaoImpl extends AbstractHibernateDao implements ObjectTypeDao {

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
