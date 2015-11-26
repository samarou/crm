package com.itechart.sample.security.hibernate.filter;

import com.itechart.sample.model.security.SecuredObject;
import org.hibernate.mapping.PersistentClass;
import org.springframework.util.Assert;

/**
 * Utilities for working with security filters
 *
 * @author andrei.samarou
 */
public class SecurityFilterUtils {

    private static final String FILTER_NAME_PREFIX = "security_";

    public static boolean isFilterableEntity(Class entityClass) {
        return entityClass != null && SecuredObject.class.isAssignableFrom(entityClass);
    }

    public static boolean isFilterableEntity(PersistentClass persistentClass) {
        return persistentClass != null && isFilterableEntity(persistentClass.getMappedClass());
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends SecuredObject> getSecuredObjectClass(PersistentClass persistentClass) {
        Class entityClass = persistentClass.getMappedClass();
        if (!SecuredObject.class.isAssignableFrom(entityClass)) {
            throw new IllegalArgumentException("Entity class is not descendant of " + SecuredObject.class);
        }
        return (Class<? extends SecuredObject>) entityClass;
    }

    public static String getFilterName(PersistentClass persistentClass, FilterType filterType) {
        return getFilterName(persistentClass.getMappedClass(), filterType);
    }

    public static String getFilterName(Class entityClass, FilterType filterType) {
        Assert.notNull(entityClass, "entityClass is required");
        Assert.notNull(entityClass, "filterType is required");
        if (!isFilterableEntity(entityClass)) {
            throw new IllegalArgumentException("Entity does't allow filtering: " + entityClass);
        }
        // hibernate does not like dots in filter name
        String entityName = entityClass.getName().replace('.', '_');
        return FILTER_NAME_PREFIX + filterType.name().toLowerCase() + '_' + entityName;
    }
}
