package com.itechart.security.business.model.enums;

import lombok.Getter;

@Getter
public enum EmailType {
    WORK("Work"),
    HOME("Home"),
    OTHER("Other");
    private String name;

    public EmailType findByName(String emailType) {
        for (EmailType tel : EmailType.values()) {
            if (tel.name().equals(emailType)) {
                return tel;
            }
        }
        throw new RuntimeException("EmailType was not found for name: " + emailType);
    }

    EmailType(String name) {
        this.name = name;
    }
}
