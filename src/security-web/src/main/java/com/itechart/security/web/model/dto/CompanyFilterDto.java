package com.itechart.security.web.model.dto;

public class CompanyFilterDto extends PagingFilterDto {
	
	private String text;
	private Long employeeNumberCategoryId;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getEmployeeNumberCategoryId() {
		return employeeNumberCategoryId;
	}

	public void setEmployeeNumberCategoryId(Long employeeNumberCategoryId) {
		this.employeeNumberCategoryId = employeeNumberCategoryId;
	}

}
