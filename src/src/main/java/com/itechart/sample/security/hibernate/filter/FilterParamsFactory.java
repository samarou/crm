package com.itechart.sample.security.hibernate.filter;

/**
 * @author andrei.samarou
 */
//todo
public class FilterParamsFactory {

    public FilterParamsSource create(Class objectType) {
        return new FilterParamsSource(objectType);
    }
}
