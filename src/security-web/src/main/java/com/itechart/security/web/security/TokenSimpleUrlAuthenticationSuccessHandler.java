package com.itechart.security.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles successfully authentication.
 * Need to always return OK 200 status code as it need for REST.
 *
 * @author yauheni.putsykovich
 * @see SimpleUrlAuthenticationSuccessHandler
 */
public class TokenSimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        String context = request.getContextPath();
        String fullURL = request.getRequestURI();
        return fullURL.substring(fullURL.indexOf(context) + context.length());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String url = determineTargetUrl(request, response);
        request.getRequestDispatcher(url).forward(request, response);
    }
}
