package com.itechart.security.web.model.notification;

import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @author Andrei Samarev
 */
public class UserCreatedNotification extends Notification {

    private String toUser;
    private Collection<String> roles;

    public UserCreatedNotification(String toAddr, String toUser, Collection<String> roles) {
        super(NotificationType.USER_CREATED, toAddr);
        Assert.notNull(toUser, "toUser is required");
        this.toUser = toUser;
        this.roles = roles;
    }

    public String getToUser() {
        return toUser;
    }

    public Collection<String> getRoles() {
        return roles;
    }
}