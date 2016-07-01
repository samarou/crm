package com.itechart.scrapper.model.crm.contact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessengerAccountDto {
    private Long id;

    private Long messenger;

    private String username;
}
