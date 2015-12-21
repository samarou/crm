package com.itechart.security.hibernate.filter;

import org.hibernate.dialect.Dialect;

/**
 * Provides filter factories based on hibernate {@link Dialect}
 *
 * @author andrei.samarou
 * @see FilterFactory
 * @see Dialect
 */
public interface FilterFactoryProvider {

    FilterFactory getFactory(Dialect dialect);
}
