package com.itechart.security.business.filter;

public class CompanyFilter extends PagingFilter {
	
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String name) {
		this.text = name;
	}
}
