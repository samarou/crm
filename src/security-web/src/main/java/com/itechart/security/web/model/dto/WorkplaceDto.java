package com.itechart.security.web.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class WorkplaceDto {
    private Long id;

    private String name;

    private String position;

    private Date startDate;

    private Date endDate;

    private String comment;
}
