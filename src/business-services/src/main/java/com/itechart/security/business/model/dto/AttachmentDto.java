package com.itechart.security.business.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties({"$$hashKey", "uploadPath"})
public class AttachmentDto {
    private Long id;
    private Long contactId;
    private String name;
    private String comment;
    private Date dateUpload;
}
