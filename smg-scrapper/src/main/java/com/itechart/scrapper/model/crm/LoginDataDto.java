package com.itechart.scrapper.model.crm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDataDto {
    private String username;
    private String password;
}
