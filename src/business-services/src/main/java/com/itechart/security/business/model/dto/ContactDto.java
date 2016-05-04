package com.itechart.security.business.model.dto;

import com.itechart.security.business.model.persistent.Address;
import com.itechart.security.business.model.persistent.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ContactDto {
    private Long id;

    private String firstName;

    private String lastName;

    private Set<Email> emails;

    private Set<Address> addresses;
}
