package com.itechart.security.web.service.impl;

import com.itechart.security.web.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Utility service for sending emails
 *
 * @author andrei.samarou
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);

    private JavaMailSender mailSender;

    public void sendMail(String from, String to, String subject, String body) throws MessagingException {
        log.debug("send mail: from = {}, to = {}, subject = {}", from, to, subject);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        mailSender.send(message);
    }

    @Required
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}