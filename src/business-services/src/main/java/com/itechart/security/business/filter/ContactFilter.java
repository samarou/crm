package com.itechart.security.business.filter;

/**
 * @author yauheni.putsykovich
 */
public class ContactFilter extends PagingFilter {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
