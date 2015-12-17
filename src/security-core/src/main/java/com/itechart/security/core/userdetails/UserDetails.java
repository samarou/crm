package com.itechart.security.core.userdetails;

import org.springframework.security.core.CredentialsContainer;

/**
 * Provides detailed information about authenticated user.
 * Based on {@link org.springframework.security.core.userdetails.UserDetails}
 *
 * @author andrei.samarou
 */
public interface UserDetails extends
        org.springframework.security.core.userdetails.UserDetails,
        CredentialsContainer {

    Long getUserId();
}
