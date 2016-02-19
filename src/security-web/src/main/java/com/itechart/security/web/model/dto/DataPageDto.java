package com.itechart.security.web.model.dto;

import java.util.List;

/**
 * Data that represents page for paging view
 *
 * @author andrei.samarou
 */
public class DataPageDto<T> {

    private List<T> data;

    private int totalCount;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}