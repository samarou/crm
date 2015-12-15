package com.itechart.sample.security.hibernate;

import com.itechart.sample.model.persistent.security.Group;
import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.model.security.Permission;
import com.itechart.sample.security.hibernate.filter.FilterParamsBuilder;
import com.itechart.sample.security.hibernate.filter.FilterParamsSource;
import com.itechart.sample.security.model.TestObject;
import com.itechart.sample.security.util.UserBuilder;
import com.itechart.sample.security.util.Utils;
import com.itechart.sample.service.DictionaryService;
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
    private DictionaryService dictionaryService;

    @Test
    @SuppressWarnings("unchecked")
    public void testBuildSource() {
        User user = UserBuilder.create("user").group("group1", "group2").role("Role")
                .privilege("TestObject", "WRITE").privilege("TestObject", "CREATE").build();
        Utils.authenticate(user);

        ObjectType objectType = new ObjectType();
        objectType.setId(1L);
        objectType.setName("TestObject");
        DictionaryService dictionaryService = mock(DictionaryService.class);
        when(dictionaryService.getObjectTypeByName("TestObject")).thenReturn(objectType);

        FilterParamsBuilder builder = new FilterParamsBuilder();
        builder.setDictionaryService(dictionaryService);

        FilterParamsSource source = builder.build(TestObject.class, EnumSet.of(Permission.WRITE, Permission.CREATE));

        Set<Long> principalIds = new HashSet<>();
        principalIds.add(user.getId());
        for (Group group : user.getGroups()) {
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
