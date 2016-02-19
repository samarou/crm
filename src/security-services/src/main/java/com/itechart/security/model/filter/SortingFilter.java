package com.itechart.security.model.filter;

/**
 * Filter supports loading of sorted lists
 *
 * @author andrei.samarou
 */
public class SortingFilter {

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
}