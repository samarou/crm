package com.itechart.security.web.security.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class LdapUserDetailsContextMapper extends LdapUserDetailsMapper {

    private Logger logger = LoggerFactory.getLogger(LdapUserDetailsContextMapper.class);

    @Autowired
    private UserDetailsService userDetailsService;

    public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
                                          Collection<? extends GrantedAuthority> authorities) {
        logger.debug("Load user {} from database", username);
        return userDetailsService.loadUserByUsername(username);
    }
}
