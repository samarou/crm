package com.itechart.sample.security;

import com.itechart.sample.model.security.User;
import com.itechart.sample.security.auth.GroupAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public static List<Long> getAuthenticatedPrincipalIds() {
        return getPrincipalIds(getAuthentication());
    }

    public static List<Long> getPrincipalIds(Authentication authentication) {
        List<Long> result = new ArrayList<>();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            result.add(((User) principal).getUserId());
        }
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
