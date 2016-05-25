package com.itechart.security.business.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private Long id;

    private String addressLine;

    private String zipcode;

    private String city;

    private String region;

    private Long country;
}
