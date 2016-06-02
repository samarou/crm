package com.itechart.common.model.filter;

/**
 * Filter supports pagination of lists
 *
 * @author andrei.samarou
 */
public class PagingFilter extends SortingFilter {

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