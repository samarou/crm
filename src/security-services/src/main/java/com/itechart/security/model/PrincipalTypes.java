package com.itechart.security.model;

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
