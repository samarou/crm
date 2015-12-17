package com.itechart.security.core;

import com.itechart.security.core.model.SecurityGroup;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.core.test.UserBuilder;
import com.itechart.security.core.test.util.ThrowableAssert;
import com.itechart.security.core.test.util.SecurityTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        ThrowableAssert.assertThrown(SecurityUtils::getAuthentication).hasMessage("Authentication information is not available");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", ""));
        ThrowableAssert.assertThrown(SecurityUtils::getAuthentication).hasMessage("Principal is not fully authenticated");
        Authentication authentication = SecurityTestUtils.authenticate(UserBuilder.create("user").build());
        Assert.assertEquals(authentication, SecurityUtils.getAuthentication());
    }

    @Test
    public void testGetAuthenticatedUserId() {
        ThrowableAssert.assertThrown(SecurityUtils::getAuthenticatedUserId).hasMessage("Authentication information is not available");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", ""));
        ThrowableAssert.assertThrown(SecurityUtils::getAuthenticatedUserId).hasMessage("Principal is not fully authenticated");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", "", null));
        ThrowableAssert.assertThrown(RuntimeException.class, SecurityUtils::getAuthenticatedUserId);

        SecurityUser user = UserBuilder.create("user").build();
        SecurityTestUtils.authenticate(user);
        Assert.assertEquals(user.getId(), SecurityUtils.getAuthenticatedUserId());
    }

    @Test
    public void testGetAuthenticatedPrincipalIds() {
        ThrowableAssert.assertThrown(SecurityUtils::getAuthenticatedPrincipalIds).hasMessage("Authentication information is not available");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", ""));
        ThrowableAssert.assertThrown(SecurityUtils::getAuthenticatedPrincipalIds).hasMessage("Principal is not fully authenticated");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", "", null));
        ThrowableAssert.assertThrown(RuntimeException.class, SecurityUtils::getAuthenticatedPrincipalIds);

        SecurityUser user = UserBuilder.create("user").group("group1", "group2").role("Role").build();
        Set<Long> principalIds = user.getGroups().stream().map(SecurityGroup::getId).collect(Collectors.toSet());
        principalIds.add(user.getId());
        SecurityTestUtils.authenticate(user);

        List<Long> authenticatedPrincipalIds = SecurityUtils.getAuthenticatedPrincipalIds();
        Assert.assertEquals(principalIds, new HashSet<>(authenticatedPrincipalIds));
        Assert.assertEquals(principalIds.size(), authenticatedPrincipalIds.size());

        SecurityTestUtils.authenticate(UserBuilder.create("user").role("Role").build());
        Assert.assertEquals(Collections.singletonList(user.getId()), SecurityUtils.getAuthenticatedPrincipalIds());
    }

    @Test
    public void testGetUserId() {
        ThrowableAssert.assertThrown(() -> SecurityUtils.getUserId(null));
        ThrowableAssert.assertThrown(RuntimeException.class, () -> SecurityUtils.getUserId(new UsernamePasswordAuthenticationToken("test", "")));
        SecurityUser user = UserBuilder.create("user").build();
        Assert.assertEquals(user.getId(), SecurityUtils.getUserId(SecurityTestUtils.authenticate(user)));
    }

    @Test
    public void testGetPrincipalIds() {
        ThrowableAssert.assertThrown(() -> SecurityUtils.getPrincipalIds(null));
        ThrowableAssert.assertThrown(RuntimeException.class, () -> SecurityUtils.getPrincipalIds(new UsernamePasswordAuthenticationToken("test", "")));

        SecurityUser user = UserBuilder.create("user").group("group1", "group2").role("Role").build();
        Set<Long> principalIds = user.getGroups().stream().map(SecurityGroup::getId).collect(Collectors.toSet());
        principalIds.add(user.getId());

        List<Long> authenticatedPrincipalIds = SecurityUtils.getPrincipalIds(SecurityTestUtils.authenticate(user));
        Assert.assertEquals(principalIds, new HashSet<>(authenticatedPrincipalIds));
        Assert.assertEquals(principalIds.size(), authenticatedPrincipalIds.size());
    }

    @Test
    public void testGetSecurityOperations() {
        ThrowableAssert.assertThrown(SecurityUtils::getSecurityOperations).hasMessage("Authentication information is not available");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("test", ""));
        ThrowableAssert.assertThrown(SecurityUtils::getSecurityOperations).hasMessage("Principal is not fully authenticated");
        Authentication authentication = SecurityTestUtils.authenticate(UserBuilder.create("user").build());
        Assert.assertEquals(authentication, SecurityUtils.getSecurityOperations().getAuthentication());
    }
}
