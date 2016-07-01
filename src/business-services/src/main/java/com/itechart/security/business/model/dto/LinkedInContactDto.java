package com.itechart.security.business.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class LinkedInContactDto extends ContactDto {
    public String fullName;
    public String location;
    public String industry;
    public String summary;
    public Set<SchoolDto> schools;
    public Set<LanguageDto> languages;
    public Set<ProjectDto> projects;
}
