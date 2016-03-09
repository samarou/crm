package com.itechart.security.web.model;

/**
 * @author yauheni.putsykovich
 */
public enum SubObjectTypes {
    USER("user"),
    GROUP("group");

    private String name;

    SubObjectTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
