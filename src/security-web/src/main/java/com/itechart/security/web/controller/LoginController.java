package com.itechart.security.web.controller;

import com.itechart.security.web.model.dto.LoginDataDto;
import com.itechart.security.web.model.dto.SessionInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author andrei.samarou
 */
@Controller
public class LoginController {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public SessionInfoDto login(@RequestBody LoginDataDto data, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String sessionToken = getToken(request, authentication);
        return new SessionInfoDto(authentication.getName(), sessionToken);
    }

    private String getToken(HttpServletRequest request, Authentication authentication) {
        return "token";
    }
}