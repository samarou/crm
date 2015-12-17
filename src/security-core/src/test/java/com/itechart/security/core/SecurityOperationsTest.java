package com.itechart.security.core;

import com.itechart.security.core.authority.RoleHierarchyImpl;
import com.itechart.security.core.exception.AuthenticationException;
import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.core.test.RoleBuilder;
import com.itechart.security.core.test.UserBuilder;
import com.itechart.security.core.test.util.SecurityTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author andrei.samarou
 */
public class SecurityOperationsTest {

    private RoleHierarchy roleHierarchy;

    @Test(expected = AuthenticationException.class)
    public void test() {
        Authentication authentication = SecurityTestUtils.authenticate(UserBuilder.create("user").build());
        authentication.setAuthenticated(false);
        new SecurityOperations(authentication);
    }

    @Test
    public void testGetAuthentication() {
        Authentication authentication = SecurityTestUtils.authenticate(UserBuilder.create("user").build());
        SecurityOperations operations = new SecurityOperations(authentication);
        assertEquals(authentication, operations.getAuthentication());
    }

    @Test
    public void testHasRole() {
        Authentication authentication = SecurityTestUtils.authenticate(
                UserBuilder.create("user").role("Manager").build());
        SecurityOperations operations = new SecurityOperations(authentication);

        assertTrue(operations.hasRole("Manager"));
        assertFalse(operations.hasRole("manager"));
        assertFalse(operations.hasRole("Root"));
        assertFalse(operations.hasRole("Admin"));

        operations = new SecurityOperations(authentication);
        operations.setRoleHierarchy(roleHierarchy);

        assertTrue(operations.hasRole("Manager"));
        assertTrue(operations.hasRole("Root"));
    }

    @Test
    public void testHasAnyRole() {
        Authentication authentication = SecurityTestUtils.authenticate(
                UserBuilder.create("user").role("Manager").build());
        SecurityOperations operations = new SecurityOperations(authentication);

        assertTrue(operations.hasAnyRole("Root", "Manager"));
        assertFalse(operations.hasAnyRole("Root", "manager"));
        assertFalse(operations.hasAnyRole("Root", "Admin"));

        operations = new SecurityOperations(authentication);
        operations.setRoleHierarchy(roleHierarchy);

        assertTrue(operations.hasAnyRole("Root"));
        assertTrue(operations.hasAnyRole("Manager"));
        assertTrue(operations.hasAnyRole("Root", "Admin"));
        assertFalse(operations.hasAnyRole("root", "Admin"));
    }

    @Test
    public void testHasPrivilege() {
        Authentication authentication = SecurityTestUtils.authenticate(UserBuilder.create("user")
                .role("Manager").privilege("TestObject", "WRITE").privilege("TestObject", "CREATE").build());
        SecurityOperations operations = new SecurityOperations(authentication);

        assertTrue(operations.hasPrivilege("TestObject", "WRITE"));
        assertTrue(operations.hasPrivilege("TestObject", "CREATE"));
        assertFalse(operations.hasPrivilege("TestObject", "create"));
        assertFalse(operations.hasPrivilege("TestObject", "READ"));
        assertFalse(operations.hasPrivilege("TestObject", "ADMIN"));

        operations = new SecurityOperations(authentication);
        operations.setRoleHierarchy(roleHierarchy);

        assertTrue(operations.hasPrivilege("TestObject", "WRITE"));
        assertTrue(operations.hasPrivilege("TestObject", "CREATE"));
        assertTrue(operations.hasPrivilege("TestObject", "READ"));
        assertFalse(operations.hasPrivilege("TestObject", "ADMIN"));
    }

    @Test
    public void testHasAnyPrivilege() {
        Authentication authentication = SecurityTestUtils.authenticate(UserBuilder.create("user")
                .role("Manager").privilege("TestObject", "WRITE").privilege("TestObject", "CREATE").build());
        SecurityOperations operations = new SecurityOperations(authentication);
        assertTrue(operations.hasAnyPrivilege("TestObject", "WRITE", "CREATE"));
        assertTrue(operations.hasAnyPrivilege("TestObject", "READ", "CREATE"));
        assertFalse(operations.hasAnyPrivilege("TestObject", "READ", "ADMIN"));
        assertFalse(operations.hasAnyPrivilege("TestObject", "write", "create"));

        operations = new SecurityOperations(authentication);
        operations.setRoleHierarchy(roleHierarchy);

        assertTrue(operations.hasAnyPrivilege("TestObject", "READ", "CREATE"));
        assertTrue(operations.hasAnyPrivilege("TestObject", "READ", "ADMIN"));
        assertTrue(operations.hasAnyPrivilege("TestObject", "READ"));
        assertFalse(operations.hasPrivilege("TestObject", "read"));
    }

    @Before
    public void initialize() {
        SecurityRepository securityRepository = mock(SecurityRepository.class);
        RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
        roleHierarchyImpl.setSecurityRepository(securityRepository);
        roleHierarchy = roleHierarchyImpl;

        SecurityRole rootRole = RoleBuilder.create("Root").build();
        SecurityRole userRole = RoleBuilder.create("User", rootRole).privilege("TestObject", "READ").build();
        SecurityRole managerRole = RoleBuilder.create("Manager", userRole).privilege("TestObject", "WRITE").build();
        SecurityRole adminRole = RoleBuilder.create("Admin", userRole).privilege("TestObject", "WRITE").privilege("TestObject", "ADMIN").build();
        when(securityRepository.getRoles()).thenReturn(Arrays.asList(rootRole, userRole, managerRole, adminRole));

        SecurityContextHolder.clearContext();
    }

}