package com.itechart.security.business.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AttachmentDto {
    private Long id;
    private Long contactId;
    private String name;
    private String comment;
    private Date dateUpload;
}
