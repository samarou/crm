package com.itechart.sample.security;

import com.itechart.sample.model.security.User;
import com.itechart.sample.security.auth.GroupAuthority;
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
            throw new RuntimeException("Authentication information is not available");
        }
        return authentication;
    }

    public static Long getAuthenticatedUserId() {
        return getUserId(getAuthentication());
    }

    public static List<Long> getAuthenticatedPrincipalIds() {
        return getPrincipalIds(getAuthentication());
    }

    public static Long getUserId(Authentication authentication) {
        Assert.notNull(authentication, "authentication is null");
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new RuntimeException("Authenticated principal type is not " + User.class);
        }
        return ((User) principal).getUserId();
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
