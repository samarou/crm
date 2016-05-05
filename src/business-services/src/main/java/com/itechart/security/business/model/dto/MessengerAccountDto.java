package com.itechart.security.business.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessengerAccountDto {

    private Long id;

    private MessengerDto messenger;

    private String username;
}
