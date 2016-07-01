package com.itechart.scrapper.model.crm.contact;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDto {
    private Long id;

    private String name;

    private String type;
}
