package com.itechart.sample.security.hibernate.filter;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.type.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of the factory for producing of security filter configurations
 *
 * @author andrei.samarou
 */
public abstract class AbstractFilterFactory implements FilterFactory {

    @Override
    public final List<FilterConfig> createFilter(PersistentClass persistentClass) {
        if (!SecurityFilterUtils.isFilterableEntity(persistentClass)) {
            throw new IllegalArgumentException("PersistentClass " + persistentClass + " doesn't allow security filtering");
        }
        List<FilterConfig> filters = new ArrayList<>();
        for (FilterType filterType : FilterType.values()) {
            FilterCondition filterCondition = buildCondition(persistentClass, filterType);
            if (filterCondition != null) {
                String filterName = SecurityFilterUtils.getFilterName(persistentClass, filterType);
                filters.add(new FilterConfig(filterName, filterType, filterCondition.getSqlCondition(),
                        filterCondition.getParameters()));
            }
        }
        return filters;
    }

    protected abstract FilterCondition buildCondition(PersistentClass persistentClass, FilterType filterType);

    public static class FilterCondition {

        private String sqlCondition;
        private Map<String, Type> parameters;

        public String getSqlCondition() {
            return sqlCondition;
        }

        public void setSqlCondition(String sqlCondition) {
            this.sqlCondition = sqlCondition;
        }

        public Map<String, Type> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Type> parameters) {
            this.parameters = parameters;
        }
    }

}