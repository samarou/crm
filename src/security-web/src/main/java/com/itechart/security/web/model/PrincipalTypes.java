package com.itechart.security.web.model;

/**
 * @author yauheni.putsykovich
 */
public enum PrincipalTypes {
    USER("user"),
    GROUP("group");

    private String name;

    PrincipalTypes(String name) {
        this.name = name;
    }

    public String getObjectType() {
        return name;
    }
}
