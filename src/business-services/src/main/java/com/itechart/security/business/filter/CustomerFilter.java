package com.itechart.security.business.filter;

/**
 * Filter for user data
 *
 * @author andrei.samarou
 */
public class CustomerFilter extends PagingFilter {
    private String text;

    public CustomerFilter() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}