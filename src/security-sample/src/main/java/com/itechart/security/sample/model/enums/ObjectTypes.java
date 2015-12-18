package com.itechart.security.sample.model.enums;

/**
 * Enumeration of business object types
 *
 * @author andrei.samarou
 */
public enum ObjectTypes {

    CUSTOMER("sample.Customer"),
    ORDER("sample.Order");

    private String name;

    ObjectTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ObjectTypes findByName(String objectTypeName) {
        if (objectTypeName != null) {
            for (ObjectTypes objectType : ObjectTypes.values()) {
                if (objectType.getName().equals(objectTypeName)) {
                    return objectType;
                }
            }
        }
        throw new RuntimeException("ObjectType was not found for name: " + objectTypeName);
    }
}