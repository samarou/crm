package com.itechart.scrapper.model.crm.roles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class ObjectTypeDto {
    private Long id;
    private String name;
    private String description;
}
