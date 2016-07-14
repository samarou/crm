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

    boolean addFields(PublicUserDto another) {
        boolean isEdited = false;
        setId(!isEmpty(getId()) ? getId() : another.getId());
        if(isEmpty(getUserName())){
            setUserName(another.getUserName());
            isEdited = true;
        }
        if(isEmpty(getEmail())){
            setEmail(another.getEmail());
            isEdited = true;
        }
        if(isEmpty(getFirstName())){
            setFirstName(another.getFirstName());
            isEdited = true;
        }
        if(isEmpty(getLastName())){
            setLastName(another.getLastName());
            isEdited = true;
        }
        return isEdited;
    }
}
