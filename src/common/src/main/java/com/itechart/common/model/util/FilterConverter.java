package com.itechart.common.model.util;

import com.itechart.common.model.filter.PagingFilter;
import com.itechart.common.model.filter.SortingFilter;
import com.itechart.common.model.filter.TextFilter;
import com.itechart.common.model.filter.dto.PagingFilterDto;
import com.itechart.common.model.filter.dto.SortingFilterDto;
import com.itechart.common.model.filter.dto.TextFilterDto;

public class FilterConverter {

    public static <T extends SortingFilter> T convert(T filter, SortingFilterDto dto) {
        filter.setSortProperty(dto.getSortProperty());
        filter.setSortAsc(dto.isSortAsc());
        return filter;
    }

    public static <T extends PagingFilter> T convert(T entity, PagingFilterDto dto) {
        T result = convert(entity, (SortingFilterDto) dto);
        result.setFrom(dto.getFrom());
        result.setCount(dto.getCount());
        return result;
    }

    public static <T extends TextFilter> T convert(T entity, TextFilterDto dto) {
        T result = convert(entity, (PagingFilterDto) dto);
        result.setText(dto.getText());
        return result;
    }

}
