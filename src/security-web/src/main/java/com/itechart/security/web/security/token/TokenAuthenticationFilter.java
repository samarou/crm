package com.itechart.security.web.security.token;

import com.itechart.security.web.exception.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication filter processes requests to stateless services,
 * extracts and validates authentication tokens, performs authentication
 * through {@link AuthenticationManager}.
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private static final String HEADER_AUTH_TOKEN = "X-Auth-Token";

    private AuthenticationManager authenticationManager;

    private TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            authenticate((HttpServletRequest) request);
            chain.doFilter(request, response);
        } catch (AuthenticationException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    public Authentication authenticate(HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(HEADER_AUTH_TOKEN);
        logger.debug("Process token {} for request {}", token, request.getQueryString());
        if (token == null) {
            throw new InvalidTokenException("Authentication token is required");
        }
        TokenData tokenData = tokenService.parseToken(token);
        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr != null && !remoteAddr.equals(tokenData.getRemoteAddr())) {
            logger.warn("IP address from token ({0}) differs from client IP ({1})", tokenData.getRemoteAddr(), remoteAddr);
            throw new InvalidTokenException("Invalid authentication token attributes");
        }
        Authentication authentication = new TokenAuthentication(token);
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}