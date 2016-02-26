package com.itechart.security.web.service;

import javax.mail.MessagingException;

/**
 * Utility service for sending emails
 *
 * @author andrei.samarou
 */
public interface MailService {

    void sendMail(String from, String to, String subject, String body) throws MessagingException;
}
