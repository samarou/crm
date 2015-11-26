package com.itechart.sample.security.hibernate.filter.dialect;

import com.itechart.sample.security.hibernate.filter.AbstractFilterFactory;
import com.itechart.sample.security.hibernate.filter.FilterType;
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
        if (filterType == FilterType.ACL_PLAIN) {
            FilterCondition condition = new FilterCondition();
            condition.setSqlCondition(
                "(select ifnull(min(if(aoi.owner_id in (:principleIds), 1, ae.permission_mask & :permissionMask)), :hasPrivilege)" +
                " from acl_object_identity aoi left join acl_entry ae on aoi.id = ae.object_identity_id and ae.principal_id in (:principleIds)" +
                " where aoi.object_type_id = :objectTypeId and aoi.object_id = {alias}.id) > 0");
            Map<String, Type> parameters = new HashMap<>();
            parameters.put("principleIds", LongType.INSTANCE);
            parameters.put("permissionMask", IntegerType.INSTANCE);
            parameters.put("hasPrivilege", BooleanType.INSTANCE);
            parameters.put("objectTypeId", LongType.INSTANCE);
            condition.setParameters(parameters);
            return condition;
        }
        return null;
    }
}
