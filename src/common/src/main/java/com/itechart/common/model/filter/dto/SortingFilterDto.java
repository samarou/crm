package com.itechart.common.model.filter.dto;

import com.itechart.common.model.filter.SortingFilter;

public class SortingFilterDto{

    private String sortProperty;
    private boolean sortAsc = true;

    public String getSortProperty() {
        return sortProperty;
    }

    public void setSortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
    }

    public boolean isSortAsc() {
        return sortAsc;
    }

    public void setSortAsc(boolean sortAsc) {
        this.sortAsc = sortAsc;
    }

    public <T extends SortingFilter> T convert(T entity) {
        entity.setSortProperty(getSortProperty());
        entity.setSortAsc(isSortAsc());
        return entity;
    }
}
