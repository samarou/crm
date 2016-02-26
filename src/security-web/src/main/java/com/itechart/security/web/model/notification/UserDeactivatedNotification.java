package com.itechart.security.web.model.notification;

import org.springframework.util.Assert;

/**
 * @author Andrei Samarev
 */
public class UserDeactivatedNotification extends Notification {

    private String toUser;
    private String reason;

    public UserDeactivatedNotification(String toAddr, String toUser, String reason) {
        super(NotificationType.USER_ACTIVATED, toAddr);
        Assert.notNull(toUser, "toUser is required");
        this.toUser = toUser;
        this.reason = reason;
    }

    public String getToUser() {
        return toUser;
    }

    public String getReason() {
        return reason;
    }
}