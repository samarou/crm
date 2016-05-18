package com.itechart.security.business.filter;

public class CompanyFilter extends PagingFilter {
	
	private String text;
	private Long employeeNumberCathegoryId;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getEmployeeNumberCathegoryId() {
		return employeeNumberCathegoryId;
	}

	public void setEmployeeNumberCathegoryId(Long employeeNumberCathegoryId) {
		this.employeeNumberCathegoryId = employeeNumberCathegoryId;
	}
}
