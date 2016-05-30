package com.itechart.security.business.filter;

import com.itechart.security.model.filter.TextFilter;

public class CompanyFilter extends TextFilter {

	private Long employeeNumberCategoryId;

	public Long getEmployeeNumberCategoryId() {
		return employeeNumberCategoryId;
	}

	public void setEmployeeNumberCategoryId(Long employeeNumberCategoryId) {
		this.employeeNumberCategoryId = employeeNumberCategoryId;
	}
}
