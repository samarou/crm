package com.itechart.security.hibernate.filter;

import org.hibernate.Filter;
import org.hibernate.engine.spi.FilterDefinition;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Parameter's holder for populating of hibernate security filters
 *
 * @author andrei.samarou
 */
public class FilterParamsSource {

    private Map<String, Object> parameters;

    public FilterParamsSource() {
        parameters = new HashMap<>();
    }

    public void setObjectTypeId(Long objectTypeId) {
        parameters.put("objectTypeId", objectTypeId);
    }

    public void setUserId(Long userId) {
        parameters.put("userId", userId);
    }

    public void setPrincipleIds(Collection<Long> principleIds) {
        parameters.put("principleIds", principleIds);
    }

    public void setPermissionMask(int permissionMask) {
        parameters.put("permissionMask", permissionMask);
    }

    public void setHasPrivilege(boolean hasPrivilege) {
        parameters.put("hasPrivilege", hasPrivilege);
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
            } else if (parameterValue instanceof Collection) {
                filter.setParameterList(parameterName, (Collection) parameterValue);
            } else {
                filter.setParameter(parameterName, parameterValue);
            }
        }
    }

    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FilterParamsSource{\n");
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(";\n");
        }
        sb.append('}');
        return sb.toString();
    }
}