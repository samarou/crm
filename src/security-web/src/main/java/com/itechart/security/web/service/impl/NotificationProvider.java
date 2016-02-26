package com.itechart.security.web.service.impl;

import com.itechart.security.web.model.notification.Notification;
import com.itechart.security.web.model.notification.NotificationType;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Provider of the notification subject and body
 */
@Component
public class NotificationProvider {

    private Configuration freemarkerConfig;
    private MessageSource subjectSource;

    public String getNotificationSubject(NotificationType notificationType, Locale locale) {
        return subjectSource.getMessage(notificationType.name().toLowerCase() + ".subject", null, locale);
    }

    public String buildNotificationMessage(Notification notification, Locale locale) throws IOException, TemplateException {
        String templateFileName = getTemplateFileName(notification.getType());
        Template template = freemarkerConfig.getTemplate(templateFileName, locale);
        StringWriter writer = new StringWriter();
        template.process(notification, writer);
        return writer.toString();
    }

    private String getTemplateFileName(NotificationType type) {
        return type.name().toLowerCase() + ".tpl";
    }

    public void setFreemarkerConfig(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    public void setSubjectSource(MessageSource subjectSource) {
        this.subjectSource = subjectSource;
    }
}