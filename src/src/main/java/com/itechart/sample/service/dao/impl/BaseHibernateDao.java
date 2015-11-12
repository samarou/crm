package com.itechart.sample.service.dao.impl;

import com.itechart.sample.model.persistent.BaseEntity;
import com.itechart.sample.service.dao.BaseDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

/**
 * Base class for hibernate data access objects
 *
 * @author andrei.samarou
 */
@Repository
public abstract class BaseHibernateDao<T extends BaseEntity> extends HibernateDaoSupport implements BaseDao<T> {

    private Class<T> persistentClass;

    @Autowired
    public void init(SessionFactory sessionFactory) {
        setSessionFactory(sessionFactory);
    }

    public BaseHibernateDao() {
        persistentClass = resolvePersistentClass();
    }

    @Override
    public T get(Serializable id) {
        return getHibernateTemplate().get(getPersistentClass(), id);
    }

    @Override
    public List<T> loadAll() {
        return getHibernateTemplate().loadAll(getPersistentClass());
    }

    @Override
    public Serializable save(T object) {
        return getHibernateTemplate().save(object);
    }

    @Override
    public void saveOrUpdate(T object) {
        getHibernateTemplate().saveOrUpdate(object);
    }

    @Override
    public void update(T object) {
        getHibernateTemplate().update(object);
    }

    @Override
    public T merge(T object) {
        return getHibernateTemplate().merge(object);
    }

    @SuppressWarnings("unchecked")
    protected List<T> find(String queryString, Object... values) {
        return (List<T>) getHibernateTemplate().find(queryString, values);
    }

    @SuppressWarnings("unchecked")
    protected T findObject(String queryString, Object... values) {
        List<?> result = getHibernateTemplate().find(queryString, values);
        return !result.isEmpty() ? (T) result.get(0) : null;
    }

    protected int executeUpdate(String query, Object... values) {
        return getHibernateTemplate().bulkUpdate(query, values);
    }

    @SuppressWarnings("unchecked")
    private Class<T> resolvePersistentClass() {
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

    protected Class<T> getPersistentClass() {
        return persistentClass;
    }
}
