package com.itechart.security.web.controller;

import com.itechart.security.core.SecurityUtils;
import com.itechart.security.core.authority.RoleAuthority;
import com.itechart.security.core.userdetails.UserDetails;
import com.itechart.security.web.model.dto.LoginDataDto;
import com.itechart.security.web.model.dto.SessionInfoDto;
import com.itechart.security.web.security.token.TokenData;
import com.itechart.security.web.security.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    public SessionInfoDto login(@RequestBody LoginDataDto data, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = SecurityUtils.getUserDetails(authentication);
        TokenData tokenData = new TokenData();
        tokenData.setUsername(userDetails.getUsername());
        tokenData.setRemoteAddr(request.getRemoteAddr());
        String token = tokenService.generateToken(tokenData);
        tokenService.setTokenToResponse(response, token);

        return getRoles();
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logout(HttpServletResponse response) {
        tokenService.deleteTokenFromResponse(response);
    }

    @ResponseBody
    @RequestMapping(value = "/login/check", method = RequestMethod.GET)
    public boolean getLoginStatus(HttpServletRequest request) {
        String token = tokenService.getTokenFromRequest(request);
        return !Objects.isNull(token);
    }


    @ResponseBody
    @RequestMapping(value = "/login/roles", method = RequestMethod.GET)
    public SessionInfoDto getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SessionInfoDto sessionInfo = new SessionInfoDto();
        sessionInfo.setUsername(authentication.getName());
        sessionInfo.setRoles(getRoleNames(authentication));
        return sessionInfo;
    }

    private Set<String> getRoleNames(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            return Collections.emptySet();
        }
        return authorities.stream()
                .filter(grantedAuthority -> grantedAuthority instanceof RoleAuthority)
                .map(grantedAuthority -> ((RoleAuthority) grantedAuthority).getRole())
                .collect(Collectors.toSet());
    }
}