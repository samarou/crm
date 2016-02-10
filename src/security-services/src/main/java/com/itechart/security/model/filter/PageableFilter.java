package com.itechart.security.model.filter;

/**
 * Filter supports pagination of lists
 *
 * @author andrei.samarou
 */
public class PageableFilter extends SortableFilter {

    private Integer from;
    private Integer count;

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}