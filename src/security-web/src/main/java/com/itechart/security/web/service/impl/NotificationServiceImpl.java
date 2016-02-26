package com.itechart.security.web.service.impl;

import com.itechart.security.web.model.notification.Notification;
import com.itechart.security.web.model.notification.UserActivatedNotification;
import com.itechart.security.web.model.notification.UserCreatedNotification;
import com.itechart.security.web.model.notification.UserDeactivatedNotification;
import com.itechart.security.web.service.MailService;
import com.itechart.security.web.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;

/**
 * Service sends email notifications
 *
 * @author Andrei Samarev
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private MailService mailService;

    private NotificationProvider notificationProvider;

    private TaskExecutor taskExecutor;

    @Value("${notification.from}")
    private String fromAddr;

    @Override
    public void sendUserCreatedNotification(String toAddr, String toUser, Collection<String> roles) {
        sendNotification(new UserCreatedNotification(toAddr, toUser, roles));
    }

    @Override
    public void sendUserActivatedNotification(String toAddr, String toUser) {
        sendNotification(new UserActivatedNotification(toAddr, toUser));
    }

    @Override
    public void sendUserDeactivatedNotification(String toAddr, String toUser, String reason) {
        sendNotification(new UserDeactivatedNotification(toAddr, toUser, reason));
    }

    public void sendNotification(Notification notification) {
        try {
            String subject = notificationProvider.getNotificationSubject(notification.getType(), DEFAULT_LOCALE);
            String message = notificationProvider.buildNotificationMessage(notification, DEFAULT_LOCALE);
            sendMailAsync(notification.getToAddr(), subject, message);
        } catch (Exception e) {
            log.error("Exception occured while sending notification: " + e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void sendMailAsync(final String toAddr, final String subject, final String message) {
        taskExecutor.execute(() -> sendMail(toAddr, subject, message));
    }

    private void sendMail(String toAddr, String subject, String message) {
        try {
            log.debug("Send mail to {}", toAddr);
            mailService.sendMail(fromAddr, toAddr, subject, message);
        } catch (Exception e) {
            log.error("Mail sending failed, cause: " + e.getMessage(), e);
        }
    }

    @Required
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Required
    public void setNotificationProvider(NotificationProvider notificationProvider) {
        this.notificationProvider = notificationProvider;
    }

    @Required
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

}