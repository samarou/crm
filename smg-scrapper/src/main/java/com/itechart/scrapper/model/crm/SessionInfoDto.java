package com.itechart.scrapper.model.crm;

import lombok.Data;

import java.util.Set;

@Data
public class SessionInfoDto {

    private String username;
    private Set<String> roles;
    private String token;
}
