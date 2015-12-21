package com.itechart.security.hibernate.filter;

import org.hibernate.dialect.Dialect;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andrei.samarou
 */
public class TestFilterFactoryProvider implements FilterFactoryProvider {

    public FilterFactory getFactory(Dialect dialect) {
        return new TestFilterFactory();
    }

    private static class TestFilterFactory extends AbstractFilterFactory {

        @Override
        protected FilterCondition buildCondition(PersistentClass persistentClass, FilterType filterType) {
            FilterCondition condition = new FilterCondition();
            if (filterType == FilterType.ACL_PLAIN) {
                condition.setSqlCondition("plain sql");
            } else if (filterType == FilterType.ACL_HIERARCHY) {
                condition.setSqlCondition("hierarchy sql");
            } else {
                throw new UnsupportedOperationException();
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
}