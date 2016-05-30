package com.itechart.security.model.dto;

import com.itechart.security.model.filter.TextFilter;

public class TextFilterDto extends PagingFilterDto {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public <T extends TextFilter> T convert(T entity) {
        T result = super.convert(entity);
        result.setText(getText());
        return result;
    }
}
