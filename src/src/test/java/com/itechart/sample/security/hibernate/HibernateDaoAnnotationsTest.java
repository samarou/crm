package com.itechart.sample.security.hibernate;

import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.security.AuthenticationException;
import com.itechart.sample.security.annotation.AclFilter;
import com.itechart.sample.security.annotation.AclFilterRule;
import com.itechart.sample.security.hibernate.aop.HibernateSecuredDao;
import com.itechart.sample.security.hibernate.filter.FilterType;
import com.itechart.sample.security.hibernate.filter.SecurityFilterUtils;
import com.itechart.sample.security.model.TestObject;
import com.itechart.sample.security.service.SecuredDao;
import com.itechart.sample.security.util.UserBuilder;
import com.itechart.sample.security.util.auth.WithUser;
import com.itechart.sample.security.util.junit.ContextAwareJUnit4ClassRunner;
import com.itechart.sample.service.DictionaryService;
import com.itechart.sample.service.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

import static com.itechart.sample.security.util.junit.ThrowableAssert.assertThrown;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Unit for testing security annotations that can be applied to Hibernate DAO
 *
 * @author andrei.samarou
 */
@RunWith(ContextAwareJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:securityTestContext.xml")
@Transactional
public class HibernateDaoAnnotationsTest implements HibernateSecuredDao {

    @Autowired
    private UserService userServiceMock;
    @Autowired
    private DictionaryService dictionaryServiceMock;

    @Autowired
    private SecuredDao securedDao;
    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @WithUser("user")
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
    @WithUser("user")
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
    @AclFilter(@AclFilterRule(type = TestObject.class))
    public void testAuthenticationUnavailable() {
    }

    @Test
    @WithUser("user")
    @AclFilter(@AclFilterRule(type = TestObject.class))
    public void testFiltersDefinitions() {
        Set filterNames = getSessionFactory().getDefinedFilterNames();
        assertEquals(2, filterNames.size());

        // filter for first-level ACLs
        String plainFilterName = SecurityFilterUtils.getFilterName(TestObject.class, FilterType.ACL_PLAIN);
        FilterDefinition plainFilter = getSessionFactory().getFilterDefinition(plainFilterName);
        assertNotNull(plainFilter);

        assertEquals(plainFilter.getDefaultFilterCondition(), "(select ifnull(min(if(aoi.owner_id = :userId, 1, ae.permission_mask >= :permissionMask)), :hasPrivilege) from acl_object_identity aoi left join acl_entry ae on ae.object_identity_id = aoi.id and ae.principal_id in (:principleIds) where aoi.object_type_id = :objectTypeId and aoi.object_id = {alias}.id) > 0");

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

        // filter for ACLs hierarchy
        String hierFilterName = SecurityFilterUtils.getFilterName(TestObject.class, FilterType.ACL_HIERARCHY);
        FilterDefinition hierFilter = getSessionFactory().getFilterDefinition(hierFilterName);
        assertNotNull(hierFilter);

        assertEquals(hierFilter.getDefaultFilterCondition(), "(select ifnull(nullif(min(least(   ifnull(if(aoi1.owner_id = :userId, 1, ae1.permission_mask >= :permissionMask), 2),   ifnull(if(aoi2.owner_id = :userId, 1, ae2.permission_mask >= :permissionMask), 2),   ifnull(if(aoi3.owner_id = :userId, 1, ae3.permission_mask >= :permissionMask), 2) )), 2), :hasPrivilege)  from acl_object_identity aoi1    left join acl_object_identity aoi2 on aoi2.id = aoi1.parent_id and aoi1.inheriting = 1   left join acl_object_identity aoi3 on aoi3.id = aoi2.parent_id and aoi2.inheriting = 1   left join acl_entry ae1 on ae1.object_identity_id = aoi1.id and ae1.principal_id in (:principleIds)    left join acl_entry ae2 on ae2.object_identity_id = aoi2.id and ae2.principal_id in (:principleIds)    left join acl_entry ae3 on ae3.object_identity_id = aoi3.id and ae3.principal_id in (:principleIds)  where aoi1.object_type_id = :objectTypeId and aoi1.object_id = {alias}.id) > 0");

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

    @Test
    @WithUser("user")
    @AclFilter(@AclFilterRule(type = TestObject.class))
    public void testPlainFilterEnabled1() {
        Session session = getSessionFactory().getCurrentSession();
        assertNotNull(session.getEnabledFilter(SecurityFilterUtils.getFilterName(TestObject.class, FilterType.ACL_PLAIN)));
        assertNull(session.getEnabledFilter(SecurityFilterUtils.getFilterName(TestObject.class, FilterType.ACL_HIERARCHY)));
    }

    @Test
    @WithUser("user")
    @AclFilter(@AclFilterRule(type = TestObject.class, inherit = false))
    public void testPlainFilterEnabled2() {
        Session session = getSessionFactory().getCurrentSession();
        assertNotNull(session.getEnabledFilter(SecurityFilterUtils.getFilterName(TestObject.class, FilterType.ACL_PLAIN)));
        assertNull(session.getEnabledFilter(SecurityFilterUtils.getFilterName(TestObject.class, FilterType.ACL_HIERARCHY)));
    }

    @Test
    @WithUser("user")
    @AclFilter(@AclFilterRule(type = TestObject.class, inherit = true))
    public void testHierFilterEnabled() {
        Session session = getSessionFactory().getCurrentSession();
        assertNull(session.getEnabledFilter(SecurityFilterUtils.getFilterName(TestObject.class, FilterType.ACL_PLAIN)));
        assertNotNull(session.getEnabledFilter(SecurityFilterUtils.getFilterName(TestObject.class, FilterType.ACL_HIERARCHY)));
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

    @PostConstruct
    public void initialize() {
        User user = UserBuilder.create("user").build();
        when(userServiceMock.findByName("user")).thenReturn(user);

        ObjectType objectType = new ObjectType();
        objectType.setId(1L);
        objectType.setName("TestObject");
        when(dictionaryServiceMock.getObjectTypeByName("TestObject")).thenReturn(objectType);

        SecurityContextHolder.clearContext();
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
