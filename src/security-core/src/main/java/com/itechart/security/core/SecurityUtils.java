package com.itechart.security.core;

import com.itechart.security.core.authority.GroupAuthority;
import com.itechart.security.core.exception.AuthenticationException;
import com.itechart.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for security purposes
 *
 * @author andrei.samarou
 */
public class SecurityUtils {

    public static Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationException("Authentication information is not available");
        }
        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException("Principal is not fully authenticated");
        }
        return authentication;
    }

    public static Long getAuthenticatedUserId() {
        return getUserId(getAuthentication());
    }

    public static List<Long> getAuthenticatedPrincipalIds() {
        return getPrincipalIds(getAuthentication());
    }

    public static UserDetails getUserDetails(Authentication authentication) {
        Assert.notNull(authentication, "authentication is null");
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new RuntimeException("Authenticated principal type is not " + UserDetails.class);
        }
        return (UserDetails) principal;
    }

    public static Long getUserId(Authentication authentication) {
        return getUserDetails(authentication).getUserId();
    }

    public static List<Long> getPrincipalIds(Authentication authentication) {
        Assert.notNull(authentication, "authentication is null");
        List<Long> result = new ArrayList<>();
        result.add(getUserId(authentication));
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority instanceof GroupAuthority) {
                result.add(((GroupAuthority) authority).getGroupId());
            }
        }
        return result;
    }

    public static SecurityOperations getSecurityOperations() {
        return new SecurityOperations(getAuthentication());
    }
}
