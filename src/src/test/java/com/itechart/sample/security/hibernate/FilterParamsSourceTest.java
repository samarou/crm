package com.itechart.sample.security.hibernate;

import com.itechart.sample.security.hibernate.filter.FilterParamsSource;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.internal.FilterImpl;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * @author andrei.samarou
 */
public class FilterParamsSourceTest {

    @Test
    public void test() {
        FilterParamsSource source = new FilterParamsSource();
        source.setUserId(1L);
        source.setPrincipleIds(Collections.singletonList(1L));
        source.setObjectTypeId(2L);
        source.setPermissionMask(3);
        source.setHasPrivilege(true);

        HashMap<String, Type> parameters = new HashMap<>();
        parameters.put("userId", LongType.INSTANCE);
        parameters.put("principleIds", LongType.INSTANCE);
        parameters.put("objectTypeId", LongType.INSTANCE);
        parameters.put("permissionMask", IntegerType.INSTANCE);
        parameters.put("hasPrivilege", BooleanType.INSTANCE);

        FilterImpl filter = new FilterImpl(new FilterDefinition("test", "", parameters));
        source.populate(filter);

        assertEquals(parameters.size(), filter.getParameters().size());

        assertEquals(1L, filter.getParameter("userId"));
        assertEquals(Collections.singletonList(1L), filter.getParameter("principleIds"));
        assertEquals(2L, filter.getParameter("objectTypeId"));
        assertEquals(3, filter.getParameter("permissionMask"));
        assertEquals(true, filter.getParameter("hasPrivilege"));
    }
}
