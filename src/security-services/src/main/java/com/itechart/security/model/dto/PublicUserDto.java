package com.itechart.security.model.dto;

import com.itechart.security.model.persistent.User;

public class PublicUserDto {

    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;

    public PublicUserDto(){
    }

    public PublicUserDto(User entity) {
        if (entity != null) {
            setId(entity.getId());
            setUserName(entity.getUserName());
            setEmail(entity.getEmail());
            setFirstName(entity.getFirstName());
            setLastName(entity.getLastName());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public User convert() {
        User result = new User();
        result.setId(getId());
        result.setUserName(getUserName());
        result.setEmail(getEmail());
        result.setFirstName(getFirstName());
        result.setLastName(getLastName());
        return result;
    }
}
