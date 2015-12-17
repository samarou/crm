package com.itechart.sample.security;

import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.hibernate.aop.HibernateSecuredDao;
import com.itechart.security.core.test.UserBuilder;
import com.itechart.security.core.test.junit.ContextAwareJUnit4ClassRunner;
import org.hibernate.SessionFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.PostConstruct;

import static org.mockito.Mockito.when;

/**
 * Unit for testing security annotations that can be applied to Hibernate DAO
 *
 * @author andrei.samarou
 */
/*@RunWith(ContextAwareJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:securityTestContext.xml")
*/
@Ignore
public class HibernateDaoAnnotationsTest implements HibernateSecuredDao {

    @Autowired
    private SecurityRepository securityRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    public void testFiltersDefinitions() {

    }

    /*todo do
        @Test
        @WithUser("user")
        @AclFilter(@AclFilterRule(type = TestObject.class))
        public void testFiltersDefinitions() {
            Set filterNames = getSessionFactory().getDefinedFilterNames();
            assertEquals(4, filterNames.size());

            // filter for first-level ACLs
            String plainFilterName = getFilterName(TestObject.class, FilterType.ACL_PLAIN);
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
            String hierFilterName = getFilterName(TestObject.class, FilterType.ACL_HIERARCHY);
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

    */
    @PostConstruct
    public void initialize() {
        SecurityUser user = UserBuilder.create("user").build();
        when(securityRepository.findUserByName("user")).thenReturn(user);

        when(securityRepository.getObjectTypeIdByName("TestObject")).thenReturn(1L);
        when(securityRepository.getObjectTypeIdByName("TestObjectExt")).thenReturn(2L);

        SecurityContextHolder.clearContext();
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
