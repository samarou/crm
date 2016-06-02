package com.itechart.security.dao.impl;

import com.itechart.security.dao.BaseDao;
import com.itechart.common.model.filter.PagingFilter;
import com.itechart.common.model.filter.SortingFilter;
import com.itechart.security.model.persistent.BaseEntity;
import com.itechart.security.util.BatchExecutor;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.sql.JoinType;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for hibernate data access objects
 *
 * @author andrei.samarou
 */
@Repository
abstract class BaseHibernateDao<T extends BaseEntity> extends AbstractHibernateDao implements BaseDao<T> {

    private static final int BATCH_SIZE = 100;
    private static final int MAX_PAGE_SIZE = 100;

    private Class<T> persistentClass;

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

    @Override
    public void delete(T object) {
        getHibernateTemplate().delete(object);
    }

    @Override
    public void deleteById(Long id) {
        T entity = get(id);
        if (entity != null) {
            delete(entity);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findByIds(List<? extends Serializable> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return getHibernateTemplate().executeWithNativeSession(session -> {
            List<T> result = new ArrayList<>(ids.size());
            BatchExecutor.execute(batch -> {
                Criteria criteria = session.createCriteria(getPersistentClass());
                criteria.add(Restrictions.in(getIdPropertyName(), batch));
                criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                result.addAll(criteria.list());
            }, ids, BATCH_SIZE);
            return result;
        });
    }

    @Override
    public int count() {
        return getHibernateTemplate().executeWithNativeSession(session -> {
            Criteria criteria = session.createCriteria(getPersistentClass());
            criteria.setProjection(Projections.rowCount());
            return ((Number) criteria.uniqueResult()).intValue();
        });
    }

    @SuppressWarnings("unchecked")
    protected List<T> executePagingDistinctCriteria(Session session, Criteria criteria, PagingFilter filter) {
        // cause Hibernate execute paging queries with distinct and outer join incorrectly
        if (filter == null) {
            return criteria.list();
        }
        appendPagingFilterConditions(criteria, filter);
        if (filter.getFrom() == null && filter.getCount() == null) {
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return criteria.list();
        } else {
            if (filter.getCount() > MAX_PAGE_SIZE) {
                throw new DataRetrievalFailureException("Page size is too big. Max value: " + MAX_PAGE_SIZE);
            }
            criteria.setProjection(Projections.distinct(Projections.id()));
            List<Serializable> ids = criteria.list();
            if (CollectionUtils.isEmpty(ids)) {
                return Collections.emptyList();
            }
            if (!(criteria instanceof CriteriaImpl)) {
                throw new IllegalArgumentException("Expected " + CriteriaImpl.class);
            }
            Criteria distinct = session.createCriteria(((CriteriaImpl) criteria).getEntityOrClassName());
            distinct.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            distinct.add(Property.forName(getIdPropertyName()).in(ids));
            appendPagingFilterConditions(distinct, filter);
            distinct.setFirstResult(0);
            distinct.setMaxResults(0);
            return distinct.list();
        }
    }

    protected Criteria appendSortingFilterConditions(Criteria criteria, SortingFilter filter) {
        String sortProperty = filter.getSortProperty();
        if (sortProperty != null) {
            String[] propertyPath = sortProperty.split("\\.");
            String property = propertyPath[propertyPath.length - 1];
            if (propertyPath.length > 1) {
                String alias = addPropertyPathAliases(criteria, propertyPath);
                property = alias + '.' + property;
            } else {
                property = criteria.getAlias() + '.' + property;
            }
            criteria.addOrder(filter.isSortAsc() ? Order.asc(property) : Order.desc(property));
        }
        return criteria;
    }

    protected Criteria appendPagingFilterConditions(Criteria criteria, PagingFilter filter) {
        appendSortingFilterConditions(criteria, filter);
        if (filter.getSortProperty() == null) {
            criteria.addOrder(Order.asc(getIdPropertyName()));
        }
        if (filter.getFrom() != null) {
            criteria.setFirstResult(filter.getFrom());
        }
        if (filter.getCount() != null) {
            criteria.setMaxResults(filter.getCount());
        }
        return criteria;
    }

    private String addPropertyPathAliases(Criteria criteria, String[] propertyPath) {
        String alias = criteria.getAlias();
        for (int i = 0; i < propertyPath.length - 1; i++) {
            String property = propertyPath[i];
            if (alias != null) {
                property = alias + "." + property;
            }
            String propertyAlias = findPropertyAlias(criteria, property);
            if (propertyAlias == null) {
                propertyAlias = propertyPath[i] + '_' + propertyPath[i].hashCode();
                criteria.createAlias(property, propertyAlias, JoinType.LEFT_OUTER_JOIN);
            }
            alias = propertyAlias;
        }
        return alias;
    }

    private String findPropertyAlias(Criteria criteria, String property) {
        if (criteria instanceof CriteriaImpl) {
            Iterator<CriteriaImpl.Subcriteria> subCriterias = ((CriteriaImpl) criteria).iterateSubcriteria();
            while (subCriterias.hasNext()) {
                CriteriaImpl.Subcriteria subCriteria = subCriterias.next();
                if (subCriteria.getPath().equals(property)) {
                    return subCriteria.getAlias();
                }
            }
        }
        return null;
    }

    public String getIdPropertyName() {
        return getSessionFactory().getClassMetadata(getPersistentClass()).getIdentifierPropertyName();
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