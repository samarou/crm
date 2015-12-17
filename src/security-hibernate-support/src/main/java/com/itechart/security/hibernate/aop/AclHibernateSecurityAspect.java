package com.itechart.security.hibernate.aop;

import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.model.acl.SecuredObject;
import com.itechart.security.hibernate.filter.FilterParamsBuilder;
import com.itechart.security.hibernate.filter.FilterParamsSource;
import com.itechart.security.hibernate.filter.FilterType;
import com.itechart.security.hibernate.filter.SecurityFilterUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Aspect that intercepts all methods annotated with {@link AclFilter}
 * It enables predefined hibernate security filters on method's execution time.
 * <p>
 * Note: Because we need to work with the hibernate session, there is requirement
 * for advised classes: they have to be subclasses of {@link HibernateSecuredDao}
 * or any <code>HibernateDaoSupport</code>.
 * Be sure that methods {@link HibernateSecuredDao#getSessionFactory()}
 * or <code>HibernateDaoSupport.getHibernateTemplate</code> is not hidden by
 * any proxy factory
 *
 * @author andrei.samarou
 * @see com.itechart.security.hibernate.SecurityIntegrator
 * @see HibernateSecuredDao
 */
@Aspect
@Configurable
public class AclHibernateSecurityAspect {

    private static final Logger logger = LoggerFactory.getLogger(AclHibernateSecurityAspect.class);

    private static final Map<Method, Set<FilterConfig>> methodFilters = new ConcurrentHashMap<>();

    private FilterParamsBuilder filterParamsBuilder;

    @Around("@annotation(aclFilter) && (within(HibernateSecuredDao+) || within(org.springframework.orm..HibernateDaoSupport+))")
    public Object applySecurityFilter(ProceedingJoinPoint joinPoint, AclFilter aclFilter) throws Throwable {
        Set<FilterConfig> filters = null;
        SessionFactory sessionFactory = null;
        try {
            if (aclFilter == null) {
                throw new IllegalStateException("Advised method doesn't have annotation " + AclFilter.class);
            }
            Method method = getDeclaredMethod(joinPoint);

            // check filters already in cache
            filters = methodFilters.get(method);

            if (filters == null) {
                filters = getFilterConfigs(joinPoint, aclFilter);

                // cache recognized filters
                methodFilters.put(method, filters);
            }

            // try to get current session factory
            sessionFactory = getSessionFactory(joinPoint);

            // enable appropriate filters before the method execution
            enableFilters(sessionFactory, filters);

            return joinPoint.proceed();

        } finally {
            if (sessionFactory != null && filters != null) {
                // disable filters after the method was called
                disableFilters(sessionFactory, filters);
            }
        }
    }

    /**
     * Enable security filters in current session factory
     */
    private void enableFilters(SessionFactory sessionFactory, Set<FilterConfig> filters) {
        Session session = sessionFactory.getCurrentSession();
        for (FilterConfig filter : filters) {
            FilterParamsSource params = filterParamsBuilder.build(filter.getObjectType(), filter.getPermissions());
            if (logger.isDebugEnabled()) {
                logger.debug("Enable security filter " + filter.getFilterName() + "\nwith parameters: " + params);
            }
            Filter hbmFilter = session.enableFilter(filter.getFilterName());
            params.populate(hbmFilter);
        }
    }

    /**
     * Disable security filters in current session factory
     */
    private void disableFilters(SessionFactory sessionFactory, Set<FilterConfig> filters) {
        Session session = sessionFactory.getCurrentSession();
        for (FilterConfig filter : filters) {
            if (logger.isDebugEnabled()) {
                logger.debug("Disable security filter " + filter.getFilterName());
            }
            session.disableFilter(filter.getFilterName());
        }
    }

    /**
     * Get configurations of filters appropriated for adviced method
     */
    private Set<FilterConfig> getFilterConfigs(ProceedingJoinPoint joinPoint, AclFilter aclFilter) throws NoSuchMethodException {
        Method method = getDeclaredMethod(joinPoint);
        List<FilterConfig> filters;
        AclFilterRule[] aclFilterRules = aclFilter.value();
        if (aclFilterRules.length != 0) {
            // create filter configs based on annotation
            filters = createFilterConfigs(aclFilterRules);
            if (logger.isDebugEnabled()) {
                logger.debug("Recognized " + filters.size() + " filter configs for " + method + " based on " + AclFilterRule.class);
            }
        } else {
            // if annotation is empty then create filter configs based on method's return types
            List<Class> filterableTypes = getFilterableTypesFromReturnType(joinPoint);
            if (CollectionUtils.isEmpty(filterableTypes)) {
                throw new IllegalStateException(
                        "Can't recognize filterable entity type from method's return type. Define least one " + AclFilterRule.class);
            }
            filters = createFilterConfigs(filterableTypes);
            if (logger.isDebugEnabled()) {
                logger.debug("Recognized " + filters.size() + " filter configs for " + method + " based on method's return types");
                for (Class type : filterableTypes) {
                    logger.debug("Filterable type: " + type);
                }
            }
        }
        // check duplicated filters
        Map<Class, FilterConfig> filtersMap = new HashMap<>(filters.size());
        for (FilterConfig filter : filters) {
            FilterConfig existed = filtersMap.get(filter.getObjectType());
            if (existed != null && !existed.equals(filter)) {
                throw new IllegalStateException("Found two security rules on same object type, "
                        + "but with different parameters in " + method + ". Check duplicated annotation @"
                        + AclFilterRule.class.getSimpleName());
            }
            filtersMap.put(filter.getObjectType(), filter);
        }
        return new HashSet<>(filtersMap.values());
    }

    /**
     * Create filter configurations based on {@link AclFilterRule} annotation
     */
    private List<FilterConfig> createFilterConfigs(AclFilterRule[] aclFilterRules) {
        List<FilterConfig> filterConfigs = new ArrayList<>();
        for (AclFilterRule aclFilterRule : aclFilterRules) {
            for (Class<? extends SecuredObject> type : aclFilterRule.type()) {
                filterConfigs.add(new FilterConfig(type, aclFilterRule.permissions(), aclFilterRule.inherit()));
            }
        }
        return filterConfigs;
    }

    /**
     * Create filter configurations based on filterable types
     * from method's return type in case of empty {@link AclFilter}
     */
    private List<FilterConfig> createFilterConfigs(List<Class> filterableTypes) {
        List<FilterConfig> filterConfigs = new ArrayList<>();
        for (Class filterableType : filterableTypes) {
            filterConfigs.add(new FilterConfig(filterableType, new Permission[]{Permission.READ}, false));
        }
        return filterConfigs;
    }

    /**
     * Try to find filterable types in method's return type recursively
     */
    private List<Class> getFilterableTypesFromReturnType(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        List<Class> filterableTypes = new ArrayList<>(2);
        Method method = getDeclaredMethod(joinPoint);
        Type returnType = method.getAnnotatedReturnType().getType();
        findAllFilterableTypes(returnType, filterableTypes);
        return filterableTypes;
    }

    /**
     * Find filterable types recursively in rootType
     */
    private void findAllFilterableTypes(Type rootType, List<Class> filterableTypes) {
        if (rootType instanceof Class) {
            Class objectType = (Class) rootType;
            if (objectType.isArray()) {
                objectType = objectType.getComponentType();
            }
            if (isFilterableType(objectType)) {
                filterableTypes.add(objectType);
            }
        } else if (rootType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) rootType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            for (Type typeArgument : typeArguments) {
                findAllFilterableTypes(typeArgument, filterableTypes);
            }
        }
    }

    /**
     * Try to get current hibernate session factory through reflection
     */
    private SessionFactory getSessionFactory(ProceedingJoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        Class<?> targetClass = target.getClass();
        if (logger.isDebugEnabled()) {
            logger.debug("Try to get SessionFactory from instance of " + targetClass);
        }
        if (target instanceof HibernateSecuredDao) {
            SessionFactory sessionFactory = ((HibernateSecuredDao) target).getSessionFactory();
            if (sessionFactory == null) {
                throw new RuntimeException("Method getSessionFactory return null");
            }
            return sessionFactory;
        }
        Class<?> superclass = targetClass;
        // check is targetClass inherited from HibernateDaoSupport
        while ((superclass = superclass.getSuperclass()) != null) {
            if (superclass.getSimpleName().equals("HibernateDaoSupport")) {
                break;
            }
        }
        if (superclass == null) {
            throw new RuntimeException("Adviced bean is not subclass of HibernateSecuredDao or HibernateDaoSupport. See "
                    + getClass().getSimpleName() + " notes for details");
        }
        try {
            Method getHibernateTemplate = targetClass.getMethod("getHibernateTemplate");
            Object hibernateTemplate = getHibernateTemplate.invoke(target);
            if (hibernateTemplate == null) {
                throw new RuntimeException("Method getHibernateTemplate return null");
            }
            // work with hibernateTemplate through reflection for excluding dependency on spring orm
            Method getSessionFactory = hibernateTemplate.getClass().getMethod("getSessionFactory");
            Object sessionFactory = getSessionFactory.invoke(hibernateTemplate);
            if (!(sessionFactory instanceof SessionFactory)) {
                throw new RuntimeException("Instance of " + SessionFactory.class
                        + " expected from getSessionFactory but it returned " + sessionFactory
                        + ". See " + getClass().getSimpleName() + " notes for details");
            }
            return (SessionFactory) sessionFactory;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Adviced bean is subclass of HibernateDaoSupport "
                    + "but it doesn't contain appropriate method getHibernateTemplate or any nested methods. See "
                    + getClass().getSimpleName() + " notes for details", e);
        }
    }

    private boolean isFilterableType(Class type) {
        return SecurityFilterUtils.isFilterableEntity(type);
    }

    private Method getDeclaredMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (method.getDeclaringClass().isInterface()) {
            // in case of jdk proxy try to get method of concrete class
            method = joinPoint.getTarget().getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        }
        return method;
    }

    @Required
    public void setFilterParamsBuilder(FilterParamsBuilder filterParamsBuilder) {
        this.filterParamsBuilder = filterParamsBuilder;
    }

    /**
     * Private utility class is used for storing immutable filter
     * configuration for each advised method
     */
    private static class FilterConfig {

        private Class objectType;
        private Set<Permission> permissions;
        private String filterName;

        public FilterConfig(Class objectType, Permission[] permissions, boolean inherit) {
            Assert.notNull(objectType);
            Assert.notEmpty(permissions);
            this.objectType = objectType;
            this.permissions = EnumSet.copyOf(Arrays.asList(permissions));
            FilterType filterType = inherit ? FilterType.ACL_HIERARCHY : FilterType.ACL_PLAIN;
            this.filterName = SecurityFilterUtils.getFilterName(objectType, filterType);
        }

        public Class getObjectType() {
            return objectType;
        }

        public Set<Permission> getPermissions() {
            return permissions;
        }

        public String getFilterName() {
            return filterName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FilterConfig that = (FilterConfig) o;
            return objectType.equals(that.objectType)
                    && permissions.equals(that.permissions)
                    && filterName.equals(that.filterName);
        }

        @Override
        public int hashCode() {
            int result = objectType.hashCode();
            result = 31 * result + permissions.hashCode();
            result = 31 * result + filterName.hashCode();
            return result;
        }
    }

}
