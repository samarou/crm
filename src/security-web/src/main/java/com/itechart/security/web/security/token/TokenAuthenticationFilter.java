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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Authentication filter that processes requests to stateless services,
 * extracts and validates authentication tokens, performs authentication
 * through {@link AuthenticationManager}.
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private AuthenticationManager authenticationManager;

    private TokenService tokenService;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            authenticate((HttpServletRequest) request, (HttpServletResponse) response);
            chain.doFilter(request, response);
        } catch (AuthenticationException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    public Authentication authenticate(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String token = tokenService.getTokenFromRequest(request);
        if (Objects.isNull(token)) throw new InvalidTokenException("Authentication token is required");
        logger.debug("Process token {} for request {}", token, request.getQueryString());
        TokenData tokenData = tokenService.parseToken(token);
        String remoteAddr = request.getRemoteAddr();
        if (remoteAddr != null && !isEqualRemoteAddresses(remoteAddr, tokenData.getRemoteAddr())) {
            logger.warn("IP address from token ({}) differs from client IP ({})", tokenData.getRemoteAddr(), remoteAddr);
            throw new InvalidTokenException("Invalid authentication token attributes");
        }

        token = tokenService.generateToken(tokenData);
        tokenService.setTokenToResponse(response, token);

        Authentication authentication = new TokenAuthentication(token);
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private boolean isEqualRemoteAddresses(String remoteAddr1, String remoteAddr2) {
        try {
            // convert addresses to InetAddress in order to remove IPv6 zone indices
            return InetAddress.getByName(remoteAddr1).equals(InetAddress.getByName(remoteAddr2));
        } catch (UnknownHostException e) {
            logger.warn("Can't parse user remote address", e);
            return true;
        }
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }
}