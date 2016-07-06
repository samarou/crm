package com.itechart.scrapper.model.crm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionInfoDto {
    private String username;
    private Set<String> roles;
    private String token;
}
