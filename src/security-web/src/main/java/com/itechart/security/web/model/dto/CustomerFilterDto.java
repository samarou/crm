package com.itechart.security.web.model.dto;

/**
 * @author yauheni.putsykovich
 */
public class CustomerFilterDto extends PagingFilterDto {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
