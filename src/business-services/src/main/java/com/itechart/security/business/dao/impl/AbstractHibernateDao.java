package com.itechart.security.business.dao.impl;

import com.itechart.common.model.filter.PagingFilter;
import com.itechart.common.model.filter.SortingFilter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;

/**
 * @author andrei.samarou
 */
@Repository
public abstract class AbstractHibernateDao<T> extends HibernateDaoSupport {

    private static final int MAX_PAGE_SIZE = 100;

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

    public boolean deleteById(Serializable id) {
        return getHibernateTemplate().executeWithNativeSession(session -> {
            Object persistentInstance = session.get(resolvePersistentClass(), id);
            if (persistentInstance != null) {
                session.delete(persistentInstance);
                return true;
            }
            return false;
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

    private String addPropertyPathAliases(Criteria criteria, String[] propertyPath) {
        String alias = criteria.getAlias();
        for (int i = 0; i < propertyPath.length - 1; i++) {
            String property = propertyPath[i];
            if (alias != null) {
                property = alias + "." + property;
            }
            String propertyAlias = findPropertyAlias(criteria, property);
            if (propertyAlias == null) {
                //need to use abs, because sometimes hashCode is a negative and '-' symbol is cause of the InvalidDataAccessResourceUsageException
                propertyAlias = propertyPath[i] + '_' + abs(propertyPath[i].hashCode());
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