package com.itechart.security.hibernate.filter.dialect;

import com.itechart.security.hibernate.filter.AbstractFilterFactory.FilterCondition;
import com.itechart.security.hibernate.filter.FilterType;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.KeyValue;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Selectable;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author andrei.samarou
 */
public class MySQLFilterFactoryTest {

    @Test
    public void testBuildCondition() {
        MySQLFilterFactory filterFactory = new MySQLFilterFactory();

        KeyValue keyValue = mock(KeyValue.class);
        List<Selectable> idColumns = Collections.singletonList((Selectable) new Column("id"));
        when(keyValue.getColumnIterator()).thenReturn(idColumns.iterator());

        PersistentClass persistentClass = mock(PersistentClass.class);
        when(persistentClass.getIdentifier()).thenReturn(keyValue);

        FilterCondition filterCondition = filterFactory.buildCondition(persistentClass, FilterType.ACL_PLAIN);
        assertEquals("(select min(if(aoi.owner_id = :userId, 1, ae.permission_mask >= :permissionMask)) and :hasPrivilege " +
                        "from acl_object_identity aoi left join acl_entry ae on ae.object_identity_id = aoi.id and ae.principal_id " +
                        "in (:principleIds) where aoi.object_type_id = :objectTypeId and aoi.object_id = {alias}.id) > 0",
                filterCondition.getSqlCondition());

        Map<String, Type> parameters = filterCondition.getParameters();
        assertEquals(parameters.get("userId"), LongType.INSTANCE);
        assertEquals(parameters.get("principleIds"), LongType.INSTANCE);
        assertEquals(parameters.get("permissionMask"), IntegerType.INSTANCE);
        assertEquals(parameters.get("hasPrivilege"), BooleanType.INSTANCE);
        assertEquals(parameters.get("objectTypeId"), LongType.INSTANCE);
        assertEquals(parameters.size(), 5);

        when(keyValue.getColumnIterator()).thenReturn(idColumns.iterator());
        filterCondition = filterFactory.buildCondition(persistentClass, FilterType.ACL_HIERARCHY);
        assertEquals("(select nullif(min(least(   ifnull(if(aoi1.owner_id = :userId, 1, ae1.permission_mask >= :permissionMask), 2),   " +
                        "ifnull(if(aoi2.owner_id = :userId, 1, ae2.permission_mask >= :permissionMask), 2),   " +
                        "ifnull(if(aoi3.owner_id = :userId, 1, ae3.permission_mask >= :permissionMask), 2) )), 2) and :hasPrivilege  " +
                        "from acl_object_identity aoi1    left join acl_object_identity aoi2 on aoi2.id = aoi1.parent_id and aoi1.inheriting = 1   " +
                        "left join acl_object_identity aoi3 on aoi3.id = aoi2.parent_id and aoi2.inheriting = 1   " +
                        "left join acl_entry ae1 on ae1.object_identity_id = aoi1.id and ae1.principal_id in (:principleIds)    " +
                        "left join acl_entry ae2 on ae2.object_identity_id = aoi2.id and ae2.principal_id in (:principleIds)    " +
                        "left join acl_entry ae3 on ae3.object_identity_id = aoi3.id and ae3.principal_id in (:principleIds)  " +
                        "where aoi1.object_type_id = :objectTypeId and aoi1.object_id = {alias}.id) > 0",
                filterCondition.getSqlCondition());

        parameters = filterCondition.getParameters();
        assertEquals(parameters.get("userId"), LongType.INSTANCE);
        assertEquals(parameters.get("principleIds"), LongType.INSTANCE);
        assertEquals(parameters.get("permissionMask"), IntegerType.INSTANCE);
        assertEquals(parameters.get("hasPrivilege"), BooleanType.INSTANCE);
        assertEquals(parameters.get("objectTypeId"), LongType.INSTANCE);
        assertEquals(parameters.size(), 5);
    }
}