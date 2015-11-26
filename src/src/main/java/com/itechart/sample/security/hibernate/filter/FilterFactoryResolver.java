package com.itechart.sample.security.hibernate.filter;

import com.itechart.sample.security.hibernate.filter.dialect.MySQLFilterFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.util.Assert;

/**
 * Filter factory resolver based on hibernate {@link Dialect}
 *
 * @author andrei.samarou
 * @see FilterFactory
 * @see Dialect
 */
public class FilterFactoryResolver {

    public static FilterFactory resolve(Dialect dialect) {
        Assert.notNull(dialect, "dialect is required");
        if (dialect instanceof MySQLDialect) {
            return new MySQLFilterFactory();
        }
        throw new RuntimeException("SQL dialect is not supported: " + dialect);
    }
}
