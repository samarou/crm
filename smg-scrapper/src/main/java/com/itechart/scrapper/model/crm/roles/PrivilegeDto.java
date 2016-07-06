package com.itechart.scrapper.model.crm.roles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class PrivilegeDto {
    private Long id;
    private ObjectTypeDto objectType;
    private ActionDto action;
}
