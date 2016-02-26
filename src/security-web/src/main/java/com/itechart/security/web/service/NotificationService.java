package com.itechart.security.web.service;

import com.itechart.security.web.model.notification.Notification;

import java.util.Collection;

/**
 * Service sends email notifications
 *
 * @author Andrei Samarev
 */
public interface NotificationService {


    void sendUserCreatedNotification(String toAddr, String toUser, Collection<String> roles);

    void sendUserActivatedNotification(String toAddr, String toUser);

    void sendUserDeactivatedNotification(String toAddr, String toUser, String reason);

    void sendNotification(Notification notification);
}