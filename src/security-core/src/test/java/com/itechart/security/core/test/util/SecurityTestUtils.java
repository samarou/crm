package com.itechart.security.core.test.util;

import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.core.userdetails.UserDetailsServiceImpl;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author andrei.samarou
 */
public class SecurityTestUtils {

    public static Authentication authenticate(SecurityUser user) {
        SecurityRepository securityRepository = Mockito.mock(SecurityRepository.class);
        Mockito.when(securityRepository.findUserByName(user.getUserName())).thenReturn(user);
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setSecurityRepository(securityRepository);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        Authentication authentication =  new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}
