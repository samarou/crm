package com.itechart.security.web.model.dto;

/**
 * @author yauheni.putsykovich
 */
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
}
