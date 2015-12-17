package com.itechart.security.hibernate.filter;

import org.hibernate.type.Type;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration of the hibernate filter
 *
 * @author andrei.samarou
 */
public class FilterConfig {

    private String name;
    private FilterType type;
    private String sqlCondition;
    private Map<String, Type> parameters;

    public FilterConfig(String name, FilterType type, String sqlCondition) {
        this(name, type, sqlCondition, null);
    }

    public FilterConfig(String name, FilterType type, String sqlCondition, Map<String, Type> parameters) {
        Assert.notNull(name);
        Assert.notNull(type);
        Assert.notNull(sqlCondition);
        this.name = name;
        this.type = type;
        this.sqlCondition = sqlCondition;
        this.parameters = new HashMap<>();
        if (parameters != null) {
            this.parameters.putAll(parameters);
        }
    }

    public String getName() {
        return name;
    }

    public FilterType getType() {
        return type;
    }

    public String getSqlCondition() {
        return sqlCondition;
    }

    public Map<String, Type> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public void addParameter(String name, Type type) {
        parameters.put(name, type);
    }

    public void addParameters(Map<String, Type> parameters) {
        this.parameters.putAll(parameters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return name.equals(((FilterConfig) o).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name + ": {" + sqlCondition + "}";
    }
}
