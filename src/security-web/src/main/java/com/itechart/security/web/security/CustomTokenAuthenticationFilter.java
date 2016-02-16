package com.itechart.security.web.security;

import com.itechart.security.web.exception.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

import static java.lang.System.currentTimeMillis;

/**
 * Custom token authentication filter to process request to rest api.
 * Executes extracting token from request and checks it.
 *
 * @author yauheni.putsykovich
 * @see AbstractAuthenticationProcessingFilter
 */
public class CustomTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger log = LoggerFactory.getLogger(CustomTokenAuthenticationFilter.class);

    private static final String HEADER_SECURITY_TOKEN = "X-Auth-Token";

    private long tokenLifetime;

    @Autowired
    private TokenWorker tokenWorker;

    public CustomTokenAuthenticationFilter(long tokeLifetime, String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        this.tokenLifetime = tokeLifetime;
    }

    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String logMessage;

        String rawToken = request.getHeader(HEADER_SECURITY_TOKEN);
        if (rawToken == null) {
            logMessage = "Authenticate impossible without authentication token";
            log.error(logMessage);
            throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", logMessage));
        }

        AuthToken token;
        try {
            log.info("Token found:" + rawToken);
            token = (AuthToken) tokenWorker.unwrapToken(rawToken);
            if (currentTimeMillis() - token.getKeyCreationTime() > tokenLifetime) {
                logMessage = "Lifetime of the token has expired";
                log.info(logMessage);
                throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", logMessage));
            }
        } catch (InvalidTokenException e) {
            log.error("Authenticate user by token error: ", e);
            throw new AuthenticationServiceException("Error | Bad Token");
        }

        if (!token.getIp().equals(request.getRemoteAddr())) {
            logMessage = MessageFormat.format("Token ip({0}) is not equals user ip({1})", token.getId(), request.getRemoteAddr());
            log.info(logMessage);
            throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", logMessage));
        }

        return new UsernamePasswordAuthenticationToken(token.getUsername(), token.getPassword(), token.getAuthorities());
    }
}