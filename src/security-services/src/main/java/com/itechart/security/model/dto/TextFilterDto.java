package com.itechart.security.model.dto;

public class TextFilterDto extends PagingFilterDto {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
