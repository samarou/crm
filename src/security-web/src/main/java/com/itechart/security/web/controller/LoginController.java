package com.itechart.security.web.controller;

import com.itechart.security.web.model.dto.LoginDataDto;
import com.itechart.security.web.model.dto.SessionInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author andrei.samarou
 */
@Controller
public class LoginController {

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @ResponseBody
    @RequestMapping(value = "/rest/login", method = RequestMethod.POST)
    public SessionInfoDto login(@RequestBody LoginDataDto data, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String sessionToken = null;
        try {
            sessionToken = getToken(request, authentication);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SessionInfoDto(authentication.getName(), sessionToken);
    }

    private String getToken(HttpServletRequest request, Authentication authentication) throws UnsupportedEncodingException {
        BytesEncryptor encryptor = Encryptors.stronger("password", "5c0744940b5c369b");
        byte[] result = encryptor.encrypt("text".getBytes("UTF-8"));
        System.out.println(new String(encryptor.decrypt(result)));

        return "token";
    }
}