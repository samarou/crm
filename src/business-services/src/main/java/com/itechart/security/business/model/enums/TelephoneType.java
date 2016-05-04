package com.itechart.security.business.model.enums;

public enum TelephoneType {
    MOBILE("Mobile"),
    WORK("Work"),
    HOME("Home"),
    FAX("Fax"),
    PAGER("Pager"),
    OTHER("Other");

    private String name;

    public static TelephoneType findByName(String typeName) {
        for (TelephoneType tel : TelephoneType.values()) {
            if (tel.name().equals(typeName)) {
                return tel;
            }
        }
        throw new RuntimeException("TelephoneType was not found for name: " + typeName);
    }

    TelephoneType(String name){
        this.name = name;
    }
}
