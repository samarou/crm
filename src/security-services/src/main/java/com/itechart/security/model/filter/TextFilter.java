package com.itechart.security.model.filter;

public class TextFilter extends PagingFilter {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
