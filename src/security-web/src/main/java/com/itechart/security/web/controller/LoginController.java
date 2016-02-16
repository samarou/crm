package com.itechart.security.web.controller;

import com.itechart.security.core.exception.AuthenticationException;
import com.itechart.security.web.model.dto.LoginDataDto;
import com.itechart.security.web.model.dto.SessionInfoDto;
import com.itechart.security.web.security.TokenWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

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
    private TokenWorker tokenWorker;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public SessionInfoDto login(@RequestBody LoginDataDto data, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Token token;
        try {
            token = tokenWorker.wrapToken(request, authentication, data.getPassword());
        } catch (UnsupportedEncodingException e) {
            logger.error("attempt login fails: ", e);
            throw new AuthenticationException("Creating authentication token failed");
        }
        return new SessionInfoDto(authentication.getName(), token.getKey());
    }
}