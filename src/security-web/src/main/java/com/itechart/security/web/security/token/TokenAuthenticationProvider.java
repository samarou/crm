package com.itechart.security.web.security.token;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * @author andrei.samarou
 */
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private TokenService tokenService;

    private UserDetailsService userDetailsService;

    private UserCache userCache;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(TokenAuthentication.class, authentication);

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof String)) {
            throw new AuthenticationServiceException("Principal is not string token");
        }
        String token = (String) principal;
        TokenData tokenData = tokenService.parseToken(token);

        String username = tokenData.getUsername();
        UserDetails user = userCache.getUserFromCache(username);

        if (user == null) {
            user = userDetailsService.loadUserByUsername(username);
            userCache.putUserInCache(user);
        }
        checkUserDetails(user);

        return new TokenAuthentication(user, user.getAuthorities());
    }

    protected void checkUserDetails(UserDetails user) {
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }
        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return TokenAuthentication.class.isAssignableFrom(authenticationType);
    }

    @Required
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Required
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }
}