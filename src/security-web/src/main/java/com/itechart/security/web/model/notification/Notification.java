package com.itechart.security.web.model.notification;

import org.springframework.util.Assert;

/**
 * Base class for all notification
 *
 * @author Andrei Samarev
 */
public abstract class Notification {

    private NotificationType type;
    private String toAddr;

    protected Notification(NotificationType type, String toAddr) {
        Assert.notNull(type, "type is required");
        Assert.notNull(toAddr, "toAddr is required");
        this.type = type;
        this.toAddr = toAddr;
    }

    public NotificationType getType() {
        return type;
    }

    public String getToAddr() {
        return toAddr;
    }
}