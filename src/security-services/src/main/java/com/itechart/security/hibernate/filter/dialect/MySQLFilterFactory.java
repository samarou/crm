package com.itechart.security.hibernate.filter.dialect;

import com.itechart.security.hibernate.filter.AbstractFilterFactory;
import com.itechart.security.hibernate.filter.FilterType;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the filter factory that build MySQL-compatible sql conditions
 *
 * @author andrei.samarou
 */
public class MySQLFilterFactory extends AbstractFilterFactory {

    @Override
    protected FilterCondition buildCondition(PersistentClass persistentClass, FilterType filterType) {
        FilterCondition condition = new FilterCondition();
        String idColumn = getIdentifierColumn(persistentClass);
        if (filterType == FilterType.ACL_PLAIN) {
            condition.setSqlCondition(
                "(select ifnull(min(if(aoi.owner_id = :userId, 1, ae.permission_mask >= :permissionMask)), :hasPrivilege)" +
                " from acl_object_identity aoi left join acl_entry ae on ae.object_identity_id = aoi.id and ae.principal_id in (:principleIds)" +
                " where aoi.object_type_id = :objectTypeId and aoi.object_id = {alias}." + idColumn + ") > 0");
        } else if (filterType == FilterType.ACL_HIERARCHY) {
            // process ACLs up to second parent (this -> parent1 -> parent2)
            condition.setSqlCondition(
                "(select ifnull(nullif(min(least(" +
                "   ifnull(if(aoi1.owner_id = :userId, 1, ae1.permission_mask >= :permissionMask), 2)," +
                "   ifnull(if(aoi2.owner_id = :userId, 1, ae2.permission_mask >= :permissionMask), 2)," +
                "   ifnull(if(aoi3.owner_id = :userId, 1, ae3.permission_mask >= :permissionMask), 2)" +
                " )), 2), :hasPrivilege) " +
                " from acl_object_identity aoi1 " +
                "   left join acl_object_identity aoi2 on aoi2.id = aoi1.parent_id and aoi1.inheriting = 1" +
                "   left join acl_object_identity aoi3 on aoi3.id = aoi2.parent_id and aoi2.inheriting = 1" +
                "   left join acl_entry ae1 on ae1.object_identity_id = aoi1.id and ae1.principal_id in (:principleIds) " +
                "   left join acl_entry ae2 on ae2.object_identity_id = aoi2.id and ae2.principal_id in (:principleIds) " +
                "   left join acl_entry ae3 on ae3.object_identity_id = aoi3.id and ae3.principal_id in (:principleIds) " +
                " where aoi1.object_type_id = :objectTypeId and aoi1.object_id = {alias}." + idColumn + ") > 0"
            );
        } else {
            throw new UnsupportedOperationException(MySQLFilterFactory.class + " supports only "
                    + FilterType.ACL_PLAIN + " and " + FilterType.ACL_HIERARCHY + " filters");
        }
        Map<String, Type> parameters = new HashMap<>();
        parameters.put("userId", LongType.INSTANCE);
        parameters.put("principleIds", LongType.INSTANCE);
        parameters.put("permissionMask", IntegerType.INSTANCE);
        parameters.put("hasPrivilege", BooleanType.INSTANCE);
        parameters.put("objectTypeId", LongType.INSTANCE);
        condition.setParameters(parameters);

        return condition;
    }
}
