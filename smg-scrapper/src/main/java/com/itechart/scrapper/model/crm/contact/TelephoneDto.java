package com.itechart.scrapper.model.crm.contact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TelephoneDto {
    private Long id;

    private String number;

    private String type;
}
