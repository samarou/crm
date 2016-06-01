package com.itechart.security.business.model.dto;

import com.itechart.security.business.model.dto.helpers.NamedEntity;
import com.itechart.security.business.model.persistent.task.Task;
import com.itechart.security.model.dto.PublicUserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author yauheni.putsykovich
 */

@Setter
@Getter
public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private PublicUserDto assignee;
    private NamedEntity status;
    private NamedEntity priority;
}

