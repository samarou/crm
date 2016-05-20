package com.itechart.security.business.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialNetworkAccountDto {

    private Long id;

    private Long socialNetwork;

    private String url;
}
