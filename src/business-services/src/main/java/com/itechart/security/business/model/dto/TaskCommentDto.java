package com.itechart.security.business.model.dto;

import com.itechart.security.model.dto.PublicUserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskCommentDto {
    private long id;
    private long taskId;
    private PublicUserDto commentAuthor;
    private long commentAuthorId;
    private String text;
    private Date dateCreated;
}
