package com.itechart.security.hibernate;

import com.itechart.security.hibernate.filter.FilterConfig;
import com.itechart.security.hibernate.filter.FilterFactory;
import com.itechart.security.hibernate.filter.FilterFactoryProvider;
import com.itechart.security.hibernate.filter.SecurityFilterUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryBuilderFactory;
import org.hibernate.boot.spi.SessionFactoryBuilderImplementor;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.config.spi.StandardConverters;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Integrator that hook into the process of a {@link SessionFactory} building.
 * <p>
 * Allows to apply ACL security constraints to hibernate {@link SessionFactory}.
 * Injects definitions of entity filters into {@link PersistentClass} and {@link Metadata}.
 * <p>
 * It requires definition of a {@link FilterFactoryProvider} implementation class in
 * hibernate configuration. Property name is {@link SecurityIntegrator#FILTER_FACTORY_PROVIDER}
 *
 * @author andrei.samarou
 * @see org.hibernate.boot.internal.MetadataImpl#getSessionFactoryBuilder()
 */
public class SecurityIntegrator implements SessionFactoryBuilderFactory {

    private static final Logger logger = LoggerFactory.getLogger(SecurityIntegrator.class);

    private static final String FILTER_FACTORY_PROVIDER = "security.filter_factory_provider";

    @Override
    public SessionFactoryBuilder getSessionFactoryBuilder(MetadataImplementor metadata, SessionFactoryBuilderImplementor defaultBuilder) {
        FilterFactoryProvider filterFactoryProvider = getFilterFactoryProvider(defaultBuilder);
        injectSecurityFilters(metadata, filterFactoryProvider);
        // return null - means that we only hook the process,
        // but don't define own SessionFactory builder
        return null;
    }

    /**
     * Create and inject security filters for hibernate entities that
     * inherits from {@link com.itechart.security.core.model.acl.SecuredObject}
     *
     * @see SecurityFilterUtils#isFilterableEntity
     */
    public void injectSecurityFilters(Metadata metadata, FilterFactoryProvider filterFactoryProvider) {
        FilterFactory filterFactory = null;
        if (filterFactoryProvider != null) {
            Dialect dialect = metadata.getDatabase().getDialect();
            filterFactory = filterFactoryProvider.getFactory(dialect);
            if (filterFactory == null) {
                throw new NullPointerException("Filter factory provider " + filterFactoryProvider.getClass()
                        + " returned null for dialect " + dialect);
            }
        }
        for (PersistentClass persistentClass : metadata.getEntityBindings()) {
            if (SecurityFilterUtils.isFilterableEntity(persistentClass)) {
                if (filterFactoryProvider == null) {
                    logger.warn("Secured entity {} was found in hibernate metadata, but filter factory provider is not configured!",
                            persistentClass.getMappedClass());
                    break;
                }
                logger.debug("Generating security filters for {}", persistentClass.getMappedClass());
                List<FilterConfig> filters = filterFactory.createFilters(persistentClass);
                for (FilterConfig filter : filters) {
                    persistentClass.addFilter(filter.getName(), filter.getSqlCondition(), false, null, null);
                    metadata.getFilterDefinitions().put(filter.getName(),
                            new FilterDefinition(filter.getName(), filter.getSqlCondition(), filter.getParameters()));
                    logger.debug("Added security filter: {}", filter.getName());
                }
            }
        }
    }

    private FilterFactoryProvider getFilterFactoryProvider(SessionFactoryBuilderImplementor defaultBuilder) {
        ServiceRegistry serviceRegistry = defaultBuilder.buildSessionFactoryOptions().getServiceRegistry();
        ConfigurationService configurationService = serviceRegistry.getService(ConfigurationService.class);
        String providerClassName = configurationService.getSetting(FILTER_FACTORY_PROVIDER, StandardConverters.STRING);
        if (providerClassName == null || providerClassName.length() == 0) {
            return null;
        }
        try {
            Class<?> providerClass = Class.forName(providerClassName);
            if (!FilterFactoryProvider.class.isAssignableFrom(providerClass)) {
                throw new RuntimeException("Filter factory provider class " + providerClassName
                        + " is not instance of " + FilterFactoryProvider.class.getName());
            }
            Constructor<?> constructor = providerClass.getConstructor();
            return (FilterFactoryProvider) constructor.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Filter factory provider class " + providerClassName + " wasn't found", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Filter factory provider class " + providerClassName + " does not have no-args constructor", e);
        } catch (Exception e) {
            throw new RuntimeException("Errors while initialization of filter factory provider", e);
        }
    }

}