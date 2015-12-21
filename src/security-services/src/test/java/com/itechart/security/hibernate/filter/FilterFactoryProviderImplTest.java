package com.itechart.security.hibernate.filter;

import com.itechart.security.hibernate.filter.dialect.MySQLFilterFactory;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.PostgreSQL94Dialect;
import org.junit.Test;

import static com.itechart.security.core.test.util.ThrowableAssert.assertThrown;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * @author andrei.samarou
 */
public class FilterFactoryProviderImplTest {

    @Test
    public void testGetFactory() {
        FilterFactoryProviderImpl provider = new FilterFactoryProviderImpl();
        assertThrown(() -> provider.getFactory(null)).isInstanceOf(IllegalArgumentException.class);
        assertThrown(() -> provider.getFactory(new Oracle10gDialect())).isInstanceOf(RuntimeException.class);
        assertThrown(() -> provider.getFactory(new PostgreSQL94Dialect())).isInstanceOf(RuntimeException.class);
        assertThat(provider.getFactory(new MySQLDialect()), instanceOf(MySQLFilterFactory.class));
    }
}
