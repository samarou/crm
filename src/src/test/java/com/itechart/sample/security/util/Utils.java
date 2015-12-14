package com.itechart.sample.security.util;

import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.security.UserDetailsServiceImpl;
import com.itechart.sample.service.UserService;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.when;

/**
 * @author andrei.samarou
 */
public class Utils {

    public static void authenticate(User user) {
        UserService userService = Mockito.mock(UserService.class);
        when(userService.findByName(user.getName())).thenReturn(user);
        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setUserService(userService);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
