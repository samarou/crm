package com.itechart.security.hibernate.filter;

import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.model.SecurityGroup;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.test.UserBuilder;
import com.itechart.security.hibernate.model.TestObject;
import com.itechart.security.core.test.util.SecurityTestUtils;
import org.junit.Test;
import org.mockito.Mock;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author andrei.samarou
 */
public class FilterParamsBuilderTest {

    @Mock
    private SecurityRepository securityRepository;

    @Test
    @SuppressWarnings("unchecked")
    public void testBuildSource() {
        SecurityUser user = UserBuilder.create("user").group("group1", "group2").role("Role")
                .privilege("TestObject", "WRITE").privilege("TestObject", "CREATE").build();
        SecurityTestUtils.authenticate(user);

        SecurityRepository securityRepository = mock(SecurityRepository.class);
        when(securityRepository.getObjectTypeIdByName("TestObject")).thenReturn(1L);

        FilterParamsBuilder builder = new FilterParamsBuilder();
        builder.setSecurityRepository(securityRepository);

        FilterParamsSource source = builder.build(TestObject.class, EnumSet.of(Permission.WRITE, Permission.CREATE));

        Set<Long> principalIds = new HashSet<>();
        principalIds.add(user.getId());
        for (SecurityGroup group : user.getGroups()) {
            principalIds.add(group.getId());
        }

        Map<String, Object> parameters = source.getParameters();
        assertEquals(5, parameters.size());

        assertEquals(user.getId(), parameters.get("userId"));
        Collection principleIds = (Collection) parameters.get("principleIds");
        assertEquals(principalIds, new HashSet(principleIds));
        assertEquals(principalIds.size(), (principleIds).size());
        assertEquals(1L, parameters.get("objectTypeId"));
        assertEquals(6, parameters.get("permissionMask"));
        assertEquals(true, parameters.get("hasPrivilege"));

        source = builder.build(TestObject.class, EnumSet.of(Permission.READ, Permission.WRITE));
        parameters = source.getParameters();

        assertEquals(3, parameters.get("permissionMask"));
        assertEquals(false, source.getParameters().get("hasPrivilege"));
    }
}
