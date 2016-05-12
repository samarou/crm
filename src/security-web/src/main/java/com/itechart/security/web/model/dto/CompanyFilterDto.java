package com.itechart.security.web.model.dto;

public class CompanyFilterDto extends PagingFilterDto {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
