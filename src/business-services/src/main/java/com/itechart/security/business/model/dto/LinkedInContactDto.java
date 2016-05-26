package com.itechart.security.business.model.dto;

import lombok.Data;

import java.util.Set;

/**
 * Created by anton.charnou on 26.05.2016.
 */
@Data
public class LinkedInContactDto extends ContactDto {
    public String fullName;
    public String location;
    public String industry;
    public String summary;
    public Set<String> skills;
    public Set<SchoolDto> schools;
    public Set<LanguageDto> languages;
    public Set<ProjectDto> projects;
}
