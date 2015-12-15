package com.itechart.sample.security;

import com.itechart.sample.model.persistent.security.Group;
import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.security.util.UserBuilder;
import com.itechart.sample.security.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

import static com.itechart.sample.security.util.junit.ThrowableAssert.assertThrown;
import static org.junit.Assert.assertEquals;

/**
 * @author andrei.samarou
 */
public class SecurityUtilsTest {

    @Before
    public void reset() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testGetAuthentication() {
        assertThrown(SecurityUtils::getAuthentication).hasMessage("Authentication information is not available");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", ""));
        assertThrown(SecurityUtils::getAuthentication).hasMessage("Principal is not fully authenticated");
        Authentication authentication = Utils.authenticate(UserBuilder.create("user").build());
        assertEquals(authentication, SecurityUtils.getAuthentication());
    }

    @Test
    public void testGetAuthenticatedUserId() {
        assertThrown(SecurityUtils::getAuthenticatedUserId).hasMessage("Authentication information is not available");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", ""));
        assertThrown(SecurityUtils::getAuthenticatedUserId).hasMessage("Principal is not fully authenticated");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", "", null));
        assertThrown(RuntimeException.class, SecurityUtils::getAuthenticatedUserId);

        User user = UserBuilder.create("user").build();
        Utils.authenticate(user);
        assertEquals(user.getId(), SecurityUtils.getAuthenticatedUserId());
    }

    @Test
    public void testGetAuthenticatedPrincipalIds() {
        assertThrown(SecurityUtils::getAuthenticatedPrincipalIds).hasMessage("Authentication information is not available");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", ""));
        assertThrown(SecurityUtils::getAuthenticatedPrincipalIds).hasMessage("Principal is not fully authenticated");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", "", null));
        assertThrown(RuntimeException.class, SecurityUtils::getAuthenticatedPrincipalIds);

        User user = UserBuilder.create("user").group("group1", "group2").role("Role").build();
        Set<Long> principalIds = user.getGroups().stream().map(Group::getId).collect(Collectors.toSet());
        principalIds.add(user.getId());
        Utils.authenticate(user);

        List<Long> authenticatedPrincipalIds = SecurityUtils.getAuthenticatedPrincipalIds();
        assertEquals(principalIds, new HashSet<>(authenticatedPrincipalIds));
        assertEquals(principalIds.size(), authenticatedPrincipalIds.size());

        Utils.authenticate(UserBuilder.create("user").role("Role").build());
        assertEquals(Collections.singletonList(user.getId()), SecurityUtils.getAuthenticatedPrincipalIds());
    }

    @Test
    public void testGetUserId() {
        assertThrown(()->SecurityUtils.getUserId(null));
        assertThrown(RuntimeException.class, () -> SecurityUtils.getUserId(new UsernamePasswordAuthenticationToken("test", "")));
        User user = UserBuilder.create("user").build();
        assertEquals(user.getId(), SecurityUtils.getUserId(Utils.authenticate(user)));
    }

    @Test
    public void testGetPrincipalIds() {
        assertThrown(()->SecurityUtils.getPrincipalIds(null));
        assertThrown(RuntimeException.class, () -> SecurityUtils.getPrincipalIds(new UsernamePasswordAuthenticationToken("test", "")));

        User user = UserBuilder.create("user").group("group1", "group2").role("Role").build();
        Set<Long> principalIds = user.getGroups().stream().map(Group::getId).collect(Collectors.toSet());
        principalIds.add(user.getId());

        List<Long> authenticatedPrincipalIds = SecurityUtils.getPrincipalIds(Utils.authenticate(user));
        assertEquals(principalIds, new HashSet<>(authenticatedPrincipalIds));
        assertEquals(principalIds.size(), authenticatedPrincipalIds.size());
    }

    @Test
    public void testGetSecurityOperations() {
        assertThrown(SecurityUtils::getSecurityOperations).hasMessage("Authentication information is not available");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", ""));
        assertThrown(SecurityUtils::getSecurityOperations).hasMessage("Principal is not fully authenticated");
        Authentication authentication = Utils.authenticate(UserBuilder.create("user").build());
        assertEquals(authentication, SecurityUtils.getSecurityOperations().getAuthentication());
    }
}
