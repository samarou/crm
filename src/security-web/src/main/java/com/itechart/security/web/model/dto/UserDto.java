package com.itechart.security.web.model.dto;

/**
 * @author andrei.samarou
 */
public class UserDto {
    private String userName;
    private String email;
    private String firstName;
    private String lastName;

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

    public void setLstName(String secondName) {
        this.lastName = secondName;
    }
}
