package com.itechart.security.web.security.remember;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class CustomRememberMeService extends TokenBasedRememberMeServices {

    private final String REMEMBER_ME_TOKEN = "remember-me";
    private boolean rememberMeCriteria;

    public CustomRememberMeService(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = this.encodeCookie(tokens);
        Cookie cookie = new Cookie(super.getCookieName(), cookieValue);
        initializeCookie(cookie, maxAge);
        response.addCookie(cookie);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        String paramValue = Boolean.toString(isRememberMeCriteria());
        if (paramValue != null && (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on") || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1"))) {
            return true;
        } else {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Did not send remember-me cookie (principal did not set parameter \'" + parameter + "\')");
            }
            return false;
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            Optional<Cookie> optionalCookie = Arrays.asList(cookies)
                    .stream()
                    .filter(cookie -> cookie.getName().equals(REMEMBER_ME_TOKEN))
                    .findFirst();
            if (optionalCookie.isPresent()) {
                token =  optionalCookie.get().getValue();
            }
        }
        return token;
    }

    public void deleteTokenFromResponse(HttpServletResponse response) {
        Cookie cookie = new Cookie(REMEMBER_ME_TOKEN, null);
        initializeCookie(cookie, 0);
        response.addCookie(cookie);
    }

    private void initializeCookie(Cookie cookie, int maxAge){
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
    }

    public boolean isRememberMeCriteria() {
        return rememberMeCriteria;
    }

    public void setRememberMeCriteria(boolean rememberMeCriteria) {
        this.rememberMeCriteria = rememberMeCriteria;
    }
}
