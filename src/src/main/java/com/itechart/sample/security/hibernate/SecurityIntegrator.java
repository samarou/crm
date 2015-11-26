package com.itechart.sample.security.hibernate;

import com.itechart.sample.security.hibernate.filter.FilterConfig;
import com.itechart.sample.security.hibernate.filter.FilterFactory;
import com.itechart.sample.security.hibernate.filter.FilterFactoryResolver;
import com.itechart.sample.security.hibernate.filter.SecurityFilterUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryBuilderFactory;
import org.hibernate.boot.spi.SessionFactoryBuilderImplementor;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.mapping.PersistentClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Integrator that hook into the process of a {@link SessionFactory} building.
 * <p>
 * Allows to apply ACL security constraints to hibernate {@link SessionFactory}.
 * Injects definitions of entity filters into {@link PersistentClass} and {@link Metadata}
 *
 * @author andrei.samarou
 * @see org.hibernate.boot.internal.MetadataImpl#getSessionFactoryBuilder()
 */
public class SecurityIntegrator implements SessionFactoryBuilderFactory {

    private static final Logger logger = LoggerFactory.getLogger(SecurityIntegrator.class);

    @Override
    public SessionFactoryBuilder getSessionFactoryBuilder(MetadataImplementor metadata, SessionFactoryBuilderImplementor defaultBuilder) {
        injectSecurityFilters(metadata);
        // returt null - means that we only hook the process,
        // but don't define own SessionFactory builder
        return null;
    }

    /**
     * Create and inject security filters for hibernate entities that
     * inherits from {@link com.itechart.sample.model.security.SecuredObject}
     *
     * @see SecurityFilterUtils#isFilterableEntity
     */
    public void injectSecurityFilters(Metadata metadata) {
        FilterFactory filterFactory = FilterFactoryResolver.resolve(metadata.getDatabase().getDialect());
        for (PersistentClass persistentClass : metadata.getEntityBindings()) {
            if (SecurityFilterUtils.isFilterableEntity(persistentClass)) {
                logger.debug("Generating security filters for " + persistentClass.getClassName());
                List<FilterConfig> filters = filterFactory.createFilter(persistentClass);
                for (FilterConfig filter : filters) {
                    persistentClass.addFilter(filter.getName(), filter.getSqlCondition(), false, null, null);
                    metadata.getFilterDefinitions().put(filter.getName(),
                            new FilterDefinition(filter.getName(), filter.getSqlCondition(), filter.getParameters()));
                    logger.debug("Added security filter: " + filter.getName());
                }
            }
        }
    }

}
