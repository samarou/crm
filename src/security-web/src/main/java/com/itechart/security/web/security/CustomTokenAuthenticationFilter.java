package com.itechart.security.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author yauheni.putsykovich
 */
public class CustomTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public final String HEADER_SECURITY_TOKEN = "X-Auth-Token";

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomTokenAuthenticationFilter.class);

    private List<String> excludedUrls;

    @Autowired
    @Qualifier("tokenService")
    private TokenServiceExtended tokenService;

    public CustomTokenAuthenticationFilter(List<String> excludedUrls, String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, AuthenticationSuccessHandler authenticationSuccessHandler) {
        super(defaultFilterProcessesUrl);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.excludedUrls = excludedUrls;
    }

    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String rawToken = request.getHeader(HEADER_SECURITY_TOKEN);
        TokenExtended token;
        String errorMessage = "Bad Token";
        try {
            if (rawToken != null) {
                LOGGER.info("Token found:" + rawToken);
                token = (TokenExtended) tokenService.unwrapToken(rawToken);
                if (token.isAlive()) {
                    String ip = tokenService.extractUserIp(request);
                    if (token.getIp().equals(ip)) {
                        return new UsernamePasswordAuthenticationToken(token.getName(), null, token.getAuthorities());
                    } else {
                        LOGGER.info(errorMessage = "Token ip({}) is not equals user ip({})", token.getId(), ip);
                    }
                } else LOGGER.info(errorMessage = "Lifetime of the token has expired");
            } else LOGGER.error(errorMessage = "Authenticate impossible without authentication token");
        } catch (Exception e) {
            LOGGER.error("Authenticate user by token error: ", e);
        }
        throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", errorMessage));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        //checking if is urls, which need to skip
        if (excludedUrls != null) {
            String uri = ((HttpServletRequest) req).getRequestURI();
            if (excludedUrls.indexOf(uri) != -1) {
                //than skip this filter
                chain.doFilter(req, res);
                return;
            }
        }
        //else, apply this filter
        super.doFilter(req, res, chain);
    }
}