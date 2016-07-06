package com.itechart.scrapper.model.crm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import static org.springframework.util.StringUtils.isEmpty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class PublicUserDto {
    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;

    void addFields(PublicUserDto another) {
        setId(!isEmpty(getId()) ? getId() : another.getId());
        setUserName(!isEmpty(getUserName()) ? getUserName() : another.getUserName());
        setEmail(!isEmpty(getEmail()) ? getEmail() : another.getEmail());
        setFirstName(!isEmpty(getFirstName()) ? getFirstName() : another.getFirstName());
        setLastName(!isEmpty(getLastName()) ? getLastName() : another.getLastName());
    }
}
