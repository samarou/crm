package com.itechart.common.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

/**
 * @author andrei.samarou
 */
@Repository
public abstract class AbstractHibernateDao<T> extends HibernateDaoSupport {

    private Class<T> persistentClass;

    @Autowired
    public void init(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    public AbstractHibernateDao() {
        persistentClass = resolvePersistentClass();
    }

    protected Class<T> getPersistentClass() {
        return persistentClass;
    }

    public String getIdPropertyName() {
        return getSessionFactory().getClassMetadata(getPersistentClass()).getIdentifierPropertyName();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(String queryString, Object... values) {
        return (List<T>) getHibernateTemplate().find(queryString, values);
    }

    public T findOne(String queryString, Object... values) {
        List<T> result = find(queryString, values);
        return result.stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    private Class<T> resolvePersistentClass() {
        //to be this то work this class must be abstract
        for (Type type = getClass().getGenericSuperclass(); type != null; ) {
            if (type instanceof ParameterizedType) {
                Type parameter = ((ParameterizedType) type).getActualTypeArguments()[0];
                if (parameter instanceof Class) {
                    return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
                } else {
                    return (Class<T>) ((ParameterizedType) ((TypeVariable<?>) parameter).getBounds()[0]).getRawType();
                }
            } else if (type instanceof Class) {
                type = ((Class<?>) type).getGenericSuperclass();
            }
        }
        throw new RuntimeException("Can't resolve persistent class");
    }

}