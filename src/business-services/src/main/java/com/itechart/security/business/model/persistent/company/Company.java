package com.itechart.security.business.model.persistent.company;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.ForeignKey;

import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.model.persistent.SecuredEntity;
import com.itechart.security.model.persistent.User;

@Entity
@Table(name = "company")
public class Company extends SecuredEntity  {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "name", nullable = false, length = 60)
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "assignee_id", foreignKey = @ForeignKey(name = "fk_company_assignee_id"))
	private User assignee;
	
	@Column(name = "logo_url", length = 255)
	private String logoUrl;
	
	@ManyToOne
	@JoinColumn(name = "company_type_id", foreignKey = @ForeignKey(name = "fk_company_type_id"))
	private CompanyType companyType;
	
	@ManyToOne
	@JoinColumn(name = "business_sphere_id", foreignKey = @ForeignKey(name = "fk_company_business_sphere_id"))
	private BusinessSphere businessSphere;
	
	@ManyToOne
	@JoinColumn(name = "emp_number_category_id", foreignKey = @ForeignKey(name = "fk_company_emp_number_category_id"))
	private EmployeeNumberCathegory employeeNumberCathegory;
	
	@Column(name = "commentary", length = 1000)
	private String commentary;
	
	//TODO address

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public CompanyType getCompanyType() {
		return companyType;
	}

	public void setCompanyType(CompanyType companyType) {
		this.companyType = companyType;
	}

	public BusinessSphere getBusinessSphere() {
		return businessSphere;
	}

	public void setBusinessSphere(BusinessSphere businessSphere) {
		this.businessSphere = businessSphere;
	}

	public EmployeeNumberCathegory getEmployeeNumberCathegory() {
		return employeeNumberCathegory;
	}

	public void setEmployeeNumberCathegory(
			EmployeeNumberCathegory employeeNumberCathegory) {
		this.employeeNumberCathegory = employeeNumberCathegory;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}
	
	@Override
    public String getObjectType() {
		return ObjectTypes.COMPANY.getName();
	}
	
}
