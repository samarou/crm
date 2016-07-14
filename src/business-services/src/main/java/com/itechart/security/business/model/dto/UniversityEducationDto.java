package com.itechart.security.business.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UniversityEducationDto {
    private Long id;

    private String name;

    private Date startDate;

    private Date endDate;

    private String type;

    private String speciality;
}
