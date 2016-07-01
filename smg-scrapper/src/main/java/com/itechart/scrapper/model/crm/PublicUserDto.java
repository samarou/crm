package com.itechart.scrapper.model.crm;

import lombok.Data;

@Data
public class PublicUserDto {
    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
}
