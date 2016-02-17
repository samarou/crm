package com.itechart.security.web.controller;

import com.itechart.security.core.SecurityUtils;
import com.itechart.security.core.userdetails.UserDetails;
import com.itechart.security.web.model.dto.LoginDataDto;
import com.itechart.security.web.model.dto.SessionInfoDto;
import com.itechart.security.web.security.token.TokenData;
import com.itechart.security.web.security.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public SessionInfoDto login(@RequestBody LoginDataDto data, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = SecurityUtils.getUserDetails(authentication);
        TokenData tokenData = new TokenData();
        tokenData.setUsername(userDetails.getUsername());
        tokenData.setRemoteAddr(request.getRemoteAddr());
        String token = tokenService.generateToken(tokenData);
        return new SessionInfoDto(authentication.getName(), token);
    }
}