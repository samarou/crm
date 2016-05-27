package com.itechart.security.web.model.dto;

public class CompanyFilterDto extends TextFilterDto {

	private Long employeeNumberCategoryId;

	public Long getEmployeeNumberCategoryId() {
		return employeeNumberCategoryId;
	}

	public void setEmployeeNumberCategoryId(Long employeeNumberCategoryId) {
		this.employeeNumberCategoryId = employeeNumberCategoryId;
	}

}
