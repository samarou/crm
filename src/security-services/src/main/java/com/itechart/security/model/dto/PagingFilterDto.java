package com.itechart.security.model.dto;

import com.itechart.security.model.filter.PagingFilter;

public class PagingFilterDto extends SortingFilterDto {

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

    public <T extends PagingFilter> T convert(T entity) {
        T result = super.convert(entity);
        result.setFrom(getFrom());
        result.setCount(getCount());
        return result;
    }
}
