package com.itechart.sample.security.expression.web;

import com.itechart.sample.security.expression.AbstractSecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @author andrei.samarou
 */
public class WebSecurityExpressionRoot extends AbstractSecurityExpressionRoot {

    public final HttpServletRequest request;

    public WebSecurityExpressionRoot(Authentication authentication, FilterInvocation filterInvocation) {
        super(authentication);
        request = filterInvocation.getRequest();
    }

    /**
     * Takes a specific IP address or a range using the IP/Netmask (e.g. 192.168.1.0/24 or 202.24.0.0/14).
     *
     * @param ipAddress the address or range of addresses from which the request must come.
     * @return true if the IP address of the current request is in the required range.
     */
    public boolean hasIpAddress(String ipAddress) {
        return new IpAddressMatcher(ipAddress).matches(request);
    }
}
