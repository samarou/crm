package com.itechart.security.web.model;

/**
 * Created by anton.charnou on 19.04.2016.
 */
public class ExceptionResponse {
    private String type;
    private String message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
