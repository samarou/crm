package com.itechart.scrapper.utils;

import com.itechart.scrapper.model.crm.LoginDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.String.format;
 class ScrapperProperties {

    private static final Logger log = LoggerFactory.getLogger(ScrapperProperties.class);

    private static Properties properties;

    private static Properties getProperties() {
        if (properties == null) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream input = classLoader.getResourceAsStream("config.properties");
                properties = new java.util.Properties();
                properties.load(input);
            } catch (IOException e) {
                log.error("can't load properties for SMG parser", e);
            }
        }
        return properties;
    }

    static String getCrmAddress(){
        return getProperties().getProperty("crm.address");
    }

    static LoginDataDto getCrmUser(String role) {
        LoginDataDto user = new LoginDataDto();
        user.setUsername(getProperties().getProperty(format("%s.profile", role)));
        user.setPassword(getProperties().getProperty(format("%s.password", role)));
        return user;
    }

    static String getSmgUser(){
        return getProperties().getProperty("smg.profile");
    }

    static String getSmgUserPassword(){
        return getProperties().getProperty("smg.profile.password");
    }
}
