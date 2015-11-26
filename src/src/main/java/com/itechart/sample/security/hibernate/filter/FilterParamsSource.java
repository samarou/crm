package com.itechart.sample.security.hibernate.filter;

import com.itechart.sample.model.security.SecuredObject;
import org.hibernate.Filter;
import org.hibernate.engine.spi.FilterDefinition;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Parameter's holder for populating of security filters
 *
 * @author andrei.samarou
 */
//todo
public class FilterParamsSource {

    Map<String, Object> parameters;

    public FilterParamsSource(Class filteredType) {
        Assert.isAssignable(SecuredObject.class, filteredType,
                "filteredType is not subclass of " + SecuredObject.class);
        parameters = new HashMap<>();


        parameters.put("", "");


        /*
            parameters.put("principleIds", LongType.INSTANCE);
            parameters.put("permissionMask", IntegerType.INSTANCE);
            parameters.put("hasPrivelege", BooleanType.INSTANCE);
            parameters.put("objectTypeId", LongType.INSTANCE);
         */


    }

    public void populate(Filter filter) {
        FilterDefinition filterDefinition = filter.getFilterDefinition();
        for (String parameterName : filterDefinition.getParameterNames()) {
            Object parameterValue = parameters.get(parameterName);
            if (parameterValue == null) {
                throw new RuntimeException("Unknown parameter '" + parameterName + "' in filter " + filter.getName());
            }
            Class<?> valueClass = parameterValue.getClass();
            if (valueClass.isArray()) {
                if (!valueClass.getComponentType().isPrimitive()) {
                    filter.setParameterList(parameterName, (Object[]) parameterValue);
                } else {
                    throw new RuntimeException("Primitive array as filter parameters isn't allowed: " + parameterName);
                }
            } else if (parameterValue instanceof Collections) {
                filter.setParameterList(parameterName, (Collection) parameterValue);
            } else {
                filter.setParameter(parameterName, parameterValue);
            }
        }
    }
}
