package com.itechart.security.business.model.dto;

import com.itechart.security.business.model.dto.company.CompanyDto;
import com.itechart.security.business.model.dto.helpers.NamedEntity;
import com.itechart.security.model.dto.PublicUserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author yauheni.putsykovich
 */

@Setter
@Getter
public class TaskDto {
    private Long id;
    private String name;
    private String location;
    private String description;
    private Date startDate;
    private Date endDate;
    private PublicUserDto assignee;
    private PublicUserDto creator;
    private NamedEntity status;
    private NamedEntity priority;
    private List<CompanyDto> companies = emptyList();
    private List<ContactDto> contacts = emptyList();
    private List<TaskCommentDto> comments = emptyList();
}

