package com.itechart.security.web.model.notification;

import org.springframework.util.Assert;

/**
 * @author Andrei Samarev
 */
public class UserActivatedNotification extends Notification {

    private String toUser;

    public UserActivatedNotification(String toAddr, String toUser) {
        super(NotificationType.USER_ACTIVATED, toAddr);
        Assert.notNull(toUser, "toUser is required");
        this.toUser = toUser;
    }

    public String getToUser() {
        return toUser;
    }
}