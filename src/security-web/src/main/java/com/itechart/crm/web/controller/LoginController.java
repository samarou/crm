package com.itechart.crm.web.controller;

import com.itechart.crm.model.LoginDetails;
import com.itechart.crm.security.TokenFactory;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    //
    private TokenFactory tokenFactory;

    @RequestMapping("/login")
    public ModelAndView login(@ModelAttribute LoginDetails loginDetails, HttpRequest request, HttpResponse response) {
        LOGGER.info("login() " + loginDetails);
        //? check if user exists
        TokenFactory.Token token = tokenFactory.create(loginDetails);
        response.getHeaders().add("token", token.value());
        return new ModelAndView("/");
    }
}
