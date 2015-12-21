package com.itechart.security.hibernate.filter;

import com.itechart.security.hibernate.filter.dialect.MySQLFilterFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.util.Assert;

/**
 * Implementation of {@link FilterFactoryProvider} is used to provide
 * filter factories according to database structure and {@link org.hibernate.SessionFactory}
 * configuration
 *
 * @author andrei.samarou
 */
public class FilterFactoryProviderImpl implements FilterFactoryProvider {

    public FilterFactory getFactory(Dialect dialect) {
        Assert.notNull(dialect, "dialect is required");
        if (dialect instanceof MySQLDialect) {
            return new MySQLFilterFactory();
        }
        throw new RuntimeException("SQL dialect is not supported: " + dialect);
    }
}