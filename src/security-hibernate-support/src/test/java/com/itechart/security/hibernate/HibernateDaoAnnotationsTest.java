package com.itechart.security.hibernate;

import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.annotation.AclFilter;
import com.itechart.security.core.annotation.AclFilterRule;
import com.itechart.security.core.exception.AuthenticationException;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.core.test.UserBuilder;
import com.itechart.security.core.test.auth.WithUser;
import com.itechart.security.core.test.junit.ContextAwareJUnit4ClassRunner;
import com.itechart.security.core.test.util.SecurityTestUtils;
import com.itechart.security.hibernate.aop.HibernateSecuredDao;
import com.itechart.security.hibernate.dao.SecuredDao;
import com.itechart.security.hibernate.filter.FilterType;
import com.itechart.security.hibernate.model.TestObject;
import com.itechart.security.hibernate.model.TestObjectExt;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static com.itechart.security.core.test.util.ThrowableAssert.assertThrown;
import static com.itechart.security.hibernate.filter.SecurityFilterUtils.getFilterName;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Unit for testing security annotations that can be applied to Hibernate DAO
 *
 * @author andrei.samarou
 */
@RunWith(ContextAwareJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:hbmSupportTestContext.xml")
@Transactional
public class HibernateDaoAnnotationsTest implements HibernateSecuredDao {

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private SecuredDao securedDao;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void testBadDeclaration() {
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration1);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration2);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration3);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration4);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration5);
        assertThrown(IllegalStateException.class, () -> securedDao.doBadDeclaration6(null));
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration7);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration8);
        assertThrown(IllegalStateException.class, securedDao::doBadDeclaration9);
    }

    @Test
    public void testGoodDeclaration() {
        securedDao.doGoodDeclaration1();
        securedDao.doGoodDeclaration2();
        securedDao.doGoodDeclaration3();
        securedDao.doGoodDeclaration4();
        securedDao.doGoodDeclaration5();
        securedDao.doGoodDeclaration6();
        securedDao.doGoodDeclaration7();
        securedDao.doGoodDeclaration8();
        securedDao.doGoodDeclaration9();
        securedDao.doGoodDeclaration10();
        securedDao.doGoodDeclaration11();
    }

    @Test(expected = AuthenticationException.class)
    public void testAuthenticationUnavailable() {
        SecurityContextHolder.clearContext();
        securedDao.doGoodDeclaration1();
    }

    @Test
    @AclFilter(@AclFilterRule(type = TestObject.class))
    public void testPlainFilterEnabled1() {
        Session session = getSessionFactory().getCurrentSession();
        assertNotNull(session.getEnabledFilter(getFilterName(TestObject.class, FilterType.ACL_PLAIN)));
        assertNull(session.getEnabledFilter(getFilterName(TestObject.class, FilterType.ACL_HIERARCHY)));
    }

    @Test
    @AclFilter(@AclFilterRule(type = TestObject.class, inherit = false))
    public void testPlainFilterEnabled2() {
        Session session = getSessionFactory().getCurrentSession();
        assertNotNull(session.getEnabledFilter(getFilterName(TestObject.class, FilterType.ACL_PLAIN)));
        assertNull(session.getEnabledFilter(getFilterName(TestObject.class, FilterType.ACL_HIERARCHY)));
    }

    @Test
    @AclFilter(@AclFilterRule(type = TestObject.class, inherit = true))
    public void testHierFilterEnabled() {
        Session session = getSessionFactory().getCurrentSession();
        assertNull(session.getEnabledFilter(getFilterName(TestObject.class, FilterType.ACL_PLAIN)));
        assertNotNull(session.getEnabledFilter(getFilterName(TestObject.class, FilterType.ACL_HIERARCHY)));
    }

    @Test
    @AclFilterRule(type = TestObject.class)
    @AclFilterRule(type = TestObjectExt.class)
    public void testMultipleFiltersEnabled() {
        Session session = getSessionFactory().getCurrentSession();
        assertNotNull(session.getEnabledFilter(getFilterName(TestObject.class, FilterType.ACL_PLAIN)));
        assertNotNull(session.getEnabledFilter(getFilterName(TestObjectExt.class, FilterType.ACL_PLAIN)));
        assertNull(session.getEnabledFilter(getFilterName(TestObject.class, FilterType.ACL_HIERARCHY)));
        assertNull(session.getEnabledFilter(getFilterName(TestObjectExt.class, FilterType.ACL_HIERARCHY)));
    }

    @Test
    public void testMultipleImplicitFiltersEnabled() {
        ArrayList<String> enabledFilters = new ArrayList<>();
        // test this case in such way because test-method can't return values
        securedDao.doDeclarationWithTwoImplicitTypes(enabledFilters);
        assertTrue(enabledFilters.containsAll(Arrays.asList(
                getFilterName(TestObject.class, FilterType.ACL_PLAIN),
                getFilterName(TestObjectExt.class, FilterType.ACL_PLAIN)
        )));
        assertEquals(2, enabledFilters.size());
    }

    @Test
    @WithUser("user")
    @AclFilter(@AclFilterRule(type = TestObject.class))
    public void testFiltersDefinitions() {
        // Test filters was created with TestFilterFactoryProvider
        Set filterNames = getSessionFactory().getDefinedFilterNames();
        assertEquals(4, filterNames.size());

        // filter for ACL_PLAIN
        String plainFilterName = getFilterName(TestObject.class, FilterType.ACL_PLAIN);
        FilterDefinition plainFilter = getSessionFactory().getFilterDefinition(plainFilterName);
        assertNotNull(plainFilter);

        assertEquals(plainFilter.getDefaultFilterCondition(), "plain sql");

        Map<String, Type> plainParameterTypes = plainFilter.getParameterTypes();
        assertEquals(5, plainParameterTypes.size());

        assertTrue(plainParameterTypes.containsKey("hasPrivilege"));
        assertEquals(BooleanType.INSTANCE, plainParameterTypes.get("hasPrivilege"));

        assertTrue(plainParameterTypes.containsKey("objectTypeId"));
        assertEquals(LongType.INSTANCE, plainParameterTypes.get("objectTypeId"));

        assertTrue(plainParameterTypes.containsKey("principleIds"));
        assertEquals(LongType.INSTANCE, plainParameterTypes.get("principleIds"));

        assertTrue(plainParameterTypes.containsKey("userId"));
        assertEquals(LongType.INSTANCE, plainParameterTypes.get("userId"));

        assertTrue(plainParameterTypes.containsKey("permissionMask"));
        assertEquals(IntegerType.INSTANCE, plainParameterTypes.get("permissionMask"));

        // filter for ACL_HIERARCHY
        String hierFilterName = getFilterName(TestObject.class, FilterType.ACL_HIERARCHY);
        FilterDefinition hierFilter = getSessionFactory().getFilterDefinition(hierFilterName);
        assertNotNull(hierFilter);

        assertEquals(hierFilter.getDefaultFilterCondition(), "hierarchy sql");

        Map<String, Type> hierParameterTypes = hierFilter.getParameterTypes();
        assertEquals(5, hierParameterTypes.size());

        assertTrue(hierParameterTypes.containsKey("hasPrivilege"));
        assertEquals(BooleanType.INSTANCE, hierParameterTypes.get("hasPrivilege"));

        assertTrue(hierParameterTypes.containsKey("objectTypeId"));
        assertEquals(LongType.INSTANCE, hierParameterTypes.get("objectTypeId"));

        assertTrue(hierParameterTypes.containsKey("principleIds"));
        assertEquals(LongType.INSTANCE, hierParameterTypes.get("principleIds"));

        assertTrue(hierParameterTypes.containsKey("userId"));
        assertEquals(LongType.INSTANCE, hierParameterTypes.get("userId"));

        assertTrue(hierParameterTypes.containsKey("permissionMask"));
        assertEquals(IntegerType.INSTANCE, hierParameterTypes.get("permissionMask"));
    }

    @After
    @SuppressWarnings("unchecked")
    public void checkFiltersDisabled() {
        // check all filters have been disabled
        SessionFactory sessionFactory = securedDao.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Set<String> filterNames = sessionFactory.getDefinedFilterNames();
        assertEquals(0, filterNames.stream().filter((s) -> session.getEnabledFilter(s) != null).count());
    }

    @Before
    public void initialize() {
        SecurityUser user = UserBuilder.create("user").build();
        when(securityRepository.findUserByName("user")).thenReturn(user);
        SecurityTestUtils.authenticate(user);

        when(securityRepository.getObjectTypeIdByName("TestObject")).thenReturn(1L);
        when(securityRepository.getObjectTypeIdByName("TestObjectExt")).thenReturn(2L);
    }

    @After
    public void clean() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
