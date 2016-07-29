package com.itechart.security.business.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskCommentDto {
    private long id;
    private long taskId;
    private String text;
    private Date dateCreated;
}
