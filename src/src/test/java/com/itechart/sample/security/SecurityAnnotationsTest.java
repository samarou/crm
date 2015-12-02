package com.itechart.sample.security;

import com.itechart.sample.model.persistent.security.Role;
import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.security.service.SecuredService;
import com.itechart.sample.security.util.RoleBuilder;
import com.itechart.sample.security.util.UserBuilder;
import com.itechart.sample.security.util.auth.WithUser;
import com.itechart.sample.security.util.junit.ContextAwareJUnit4ClassRunner;
import com.itechart.sample.service.AclService;
import com.itechart.sample.service.RoleService;
import com.itechart.sample.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.mockito.Mockito.when;

/**
 * @author andrei.samarou
 */
@RunWith(ContextAwareJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:securityTestContext.xml")
public class SecurityAnnotationsTest {

    @Autowired
    private UserService userServiceMock;
    @Autowired
    private RoleService roleServiceMock;
    @Autowired
    private AclService aclServiceMock;
    @Resource
    private SecuredService securedServiceSpy;

    @WithUser("userRoleUser")
    @PreAuthorize("hasRole('User')")
    @Test
    public void testHasRole11() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasRole('Root')")
    @Test
    public void testHasRole12() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasRole('Admin')")
    @Test(expected = AccessDeniedException.class)
    public void testHasRole21() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("hasRole('User')")
    @Test(expected = AccessDeniedException.class)
    public void testHasRole22() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasAuthority('User')")
    @Test(expected = UnsupportedOperationException.class)
    public void testHasRole31() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasAnyAuthority('User')")
    @Test(expected = UnsupportedOperationException.class)
    public void testHasRole32() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPrivilege('Object', 'READ')")
    @Test
    public void testHasPrivilege11() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPrivilege('Object', 'WRITE')")
    @Test
    public void testHasPrivilege12() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasPrivilege('Object', 'ADMIN')")
    @Test
    public void testHasPrivilege13() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasPrivilege('Object', 'READ')")
    @Test
    public void testHasPrivilege14() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasPrivilege('OBJECT', 'WRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege21() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasPrivilege('Object', 'wRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege22() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasPrivilege('Object', 'WRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege23() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPrivilege('Object', 'ADMIN')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege24() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("hasPrivilege('Object', 'READ')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege25() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("hasPrivilege('Object', 'WRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege26() {
    }

    @WithUser("userRoleOther")
    @PreAuthorize("hasPrivilege('Object', 'READ')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege27() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasAnyPrivilege('Object', 'READ')")
    @Test
    public void testHasAnyPrivilege11() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasAnyPrivilege('Object', 'READ', 'WRITE')")
    @Test
    public void testHasAnyPrivilege12() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasAnyPrivilege('Object', 'READ', 'WRITE', 'ADMIN')")
    @Test
    public void testHasAnyPrivilege13() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasAnyPrivilege('Object', 'READ')")
    @Test
    public void testHasAnyPrivilege14() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasAnyPrivilege('Object', 'READ', 'WRITE')")
    @Test
    public void testHasAnyPrivilege15() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasAnyPrivilege('Object', 'WRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasAnyPrivilege21() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasAnyPrivilege('Object', 'ADMIN')")
    @Test(expected = AccessDeniedException.class)
    public void testHasAnyPrivilege22() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("hasAnyPrivilege('Object')")
    @Test(expected = AccessDeniedException.class)
    public void testHasAnyPrivilege23() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("isAnonymous()")
    @Test(expected = AccessDeniedException.class)
    public void testIsAnonymous1() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("isAuthenticated()")
    @Test
    public void testIsAuthenticated1() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("!isAuthenticated()")
    @Test(expected = AccessDeniedException.class)
    public void testIsAuthenticated2() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("@securedServiceSpy.hasAccess(true)")
    @Test
    public void testCustomMethod1() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("@securedServiceSpy.hasAccess(false)")
    @Test(expected = AccessDeniedException.class)
    public void testCustomMethod2() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPrivilege('Object', 'WRITE') or hasRole('Admin')")
    @Test
    public void testCompound1() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPrivilege('Object', 'WRITE') and hasRole('Admin')")
    @Test(expected = AccessDeniedException.class)
    public void testCompound2() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasRole('Manager') and hasRole('Root')")
    public void testCompound3() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasRole('Manager') and !hasRole('Root')")
    @Test(expected = AccessDeniedException.class)
    public void testCompound4() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPermission(999, 'Object', 'WRITE')")
    public void testHasPermission11() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPermission(999, 'Object', 'ADMIN')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPermission12() {
    }

    @WithUser("userRoleOther")
    @PreAuthorize("hasPermission(999, 'OtherObject', 'Read')")
    public void testHasPermission13() {
    }

    @WithUser("userRoleOther")
    @PreAuthorize("hasPermission(999, 'OtherObject', 'READ')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPermission14() {
        // ACL for object 999 doesn't exist, so we check user privileges.
        // But we have mapping of privileges to permissions by name and
        // privilege's name 'Read' (in role Other) != permission name 'READ'
    }

    //todo test next cases of SecuredService
    // doPreAuthorizeByObjectId(Long objectId) {
    // doPreFilterWithObjectPropertyEqUserName(List<T> input) {
    // doPostFilterWithObjectPropertyEqUserName() {
    // doPreFilterByPermissionRead(List<T> input) {
    // doPostFilterByPermissionRead(List<T> input) {

    //todo tests for @AclObjectFilter

    /*
ArgumentCaptor<Person> peopleCaptor = ArgumentCaptor.forClass(Person.class);
verify(mock, times(2)).doSomething(peopleCaptor.capture());
List<Person> capturedPeople = peopleCaptor.getAllValues();
assertEquals("John", capturedPeople.get(0).getName());
assertEquals("Jane", capturedPeople.get(1).getName());
     */

    @Before
    public void initAuthData() {
        Role rootRole = RoleBuilder.create("Root").build();
        Role userRole = RoleBuilder.create("User", rootRole).privilege("Object", "READ").build();
        Role managerRole = RoleBuilder.create("Manager", userRole).privilege("Object", "WRITE").build();
        Role adminRole = RoleBuilder.create("Admin", userRole).privilege("Object", "WRITE").privilege("Object", "ADMIN").build();
        mock(rootRole, userRole, managerRole, adminRole);

        mock(UserBuilder.create("userEmpty").build());
        mock(UserBuilder.create("userRoleUser").role(userRole).build());
        mock(UserBuilder.create("userRoleManager").role(managerRole).build());
        mock(UserBuilder.create("userRoleAdmin").role(adminRole).build());
        mock(UserBuilder.create("userRoleOther").role("Other").privilege("OtherObject", "Read").build());
    }

    private void mock(User user) {
        when(userServiceMock.findByName(user.getName())).thenReturn(user);
    }

    private void mock(Role... role) {
        when(roleServiceMock.getRoles()).thenReturn(Arrays.asList(role));
    }

    public static void setAuthentication(String userName) {
        Authentication token = new UsernamePasswordAuthenticationToken(userName, userName);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
