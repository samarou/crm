package com.itechart.sample.security;

import com.itechart.sample.model.persistent.security.Role;
import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.security.ObjectIdentityImpl;
import com.itechart.sample.model.security.Permission;
import com.itechart.sample.model.security.SecuredObject;
import com.itechart.sample.security.service.SecuredService;
import com.itechart.sample.security.util.AclBuilder;
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
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    @Autowired
    private SecuredService securedService;
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
    @PreAuthorize("hasPrivilege('TestObject', 'READ')")
    @Test
    public void testHasPrivilege11() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPrivilege('TestObject', 'WRITE')")
    @Test
    public void testHasPrivilege12() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasPrivilege('TestObject', 'ADMIN')")
    @Test
    public void testHasPrivilege13() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasPrivilege('TestObject', 'READ')")
    @Test
    public void testHasPrivilege14() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasPrivilege('TESTobject', 'WRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege21() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasPrivilege('TestObject', 'wRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege22() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasPrivilege('TestObject', 'WRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege23() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPrivilege('TestObject', 'ADMIN')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege24() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("hasPrivilege('TestObject', 'READ')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege25() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("hasPrivilege('TestObject', 'WRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege26() {
    }

    @WithUser("userRoleOther")
    @PreAuthorize("hasPrivilege('TestObject', 'READ')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPrivilege27() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasAnyPrivilege('TestObject', 'READ')")
    @Test
    public void testHasAnyPrivilege11() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasAnyPrivilege('TestObject', 'READ', 'WRITE')")
    @Test
    public void testHasAnyPrivilege12() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasAnyPrivilege('TestObject', 'READ', 'WRITE', 'ADMIN')")
    @Test
    public void testHasAnyPrivilege13() {
    }

    @WithUser("userRoleAdmin")
    @PreAuthorize("hasAnyPrivilege('TestObject', 'READ')")
    @Test
    public void testHasAnyPrivilege14() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasAnyPrivilege('TestObject', 'READ', 'WRITE')")
    @Test
    public void testHasAnyPrivilege15() {
    }

    @WithUser("userRoleUser")
    @PreAuthorize("hasAnyPrivilege('TestObject', 'WRITE')")
    @Test(expected = AccessDeniedException.class)
    public void testHasAnyPrivilege21() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasAnyPrivilege('TestObject', 'ADMIN')")
    @Test(expected = AccessDeniedException.class)
    public void testHasAnyPrivilege22() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("hasAnyPrivilege('TestObject')")
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
    @PreAuthorize("@securedService.hasAccess(true)")
    @Test
    public void testCustomMethod1() {
    }

    @WithUser("userEmpty")
    @PreAuthorize("@securedService.hasAccess(false)")
    @Test(expected = AccessDeniedException.class)
    public void testCustomMethod2() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPrivilege('TestObject', 'WRITE') or hasRole('Admin')")
    @Test
    public void testCompound1() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPrivilege('TestObject', 'WRITE') and hasRole('Admin')")
    @Test(expected = AccessDeniedException.class)
    public void testCompound2() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasRole('Manager') and hasRole('Root')")
    @Test
    public void testCompound3() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasRole('Manager') and !hasRole('Root')")
    @Test(expected = AccessDeniedException.class)
    public void testCompound4() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPermission(999, 'TestObject', 'WRITE')")
    public void testHasPermission11() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPermission(999, 'TestObject', 'ADMIN')")
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
        // ACL for object with id=999 doesn't exist, so we check user privileges.
        // But we have mapping of privileges to permissions by name and
        // privilege's name 'Read' (in role Other) != permission name 'READ'
    }

    @Test
    public void testPreAuthorizeByReadObjectId() {
        setAuthentication("userRoleManager");
        securedService.doPreAuthorizeByReadObjectId(999L);

        setAuthentication("userRoleOther");
        try {
            securedService.doPreAuthorizeByReadObjectId(999L);
            fail("AccessDeniedException expected");
        } catch (AccessDeniedException ignored) {
        }

        Acl acl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(aclServiceMock.findAclWithAncestors(new ObjectIdentityImpl(999L, "TestObject")))
                .thenReturn(Collections.singletonList(acl));

        securedService.doPreAuthorizeByReadObjectId(999L);
    }

    @Test
    @WithUser("userEmpty")
    @SuppressWarnings("unchecked")
    public void testPreFilterWithObjectPropertyEqUserName() {
        TestObject object = new TestObject("userEmpty");
        List<TestObject> objects = Collections.singletonList(object);

        reset(unwrap(securedServiceSpy));
        assertEquals(objects, securedServiceSpy.doPreFilterWithObjectPropertyEqUserName(objects));
        verify(unwrap(securedServiceSpy)).doPreFilterWithObjectPropertyEqUserName(objects);

        reset(unwrap(securedServiceSpy));
        object.setProperty("notUserName");
        List<TestObject> input = new ArrayList<>(objects); // mutable list expected
        List<TestObject> result = securedServiceSpy.doPreFilterWithObjectPropertyEqUserName(input);
        // while filtering we change input collection, so check that is same object by reference
        verify(unwrap(securedServiceSpy)).doPreFilterWithObjectPropertyEqUserName(same(input));
        verify(unwrap(securedServiceSpy)).doPreFilterWithObjectPropertyEqUserName(eq(result));
        assertTrue(result.isEmpty());

        try {
            securedServiceSpy.doPreFilterWithObjectPropertyEqUserName(objects);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException ignored) {
            // Exception when we try to modify/filter immutable list
        }
    }

    @Test
    @WithUser("userEmpty")
    public void testPostFilterWithObjectPropertyEqUserName() {
        TestObject object = new TestObject("userEmpty");
        List<TestObject> objects = Collections.singletonList(object);

        reset(unwrap(securedServiceSpy));
        assertEquals(objects, securedServiceSpy.doPostFilterWithObjectPropertyEqUserName(objects));
        verify(unwrap(securedServiceSpy)).doPostFilterWithObjectPropertyEqUserName(objects);

        reset(unwrap(securedServiceSpy));
        object.setProperty("notUserName");
        List<TestObject> result = securedServiceSpy.doPostFilterWithObjectPropertyEqUserName(objects);
        verify(unwrap(securedServiceSpy)).doPostFilterWithObjectPropertyEqUserName(objects);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testPreFilterByPermissionRead() {
        setAuthentication("userRoleUser");
        try {
            List<Object> nonSecuredObjects = Collections.singletonList(new Object());
            securedServiceSpy.doPreFilterByPermissionRead(nonSecuredObjects);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
            // only successors of SecuredObject are allowed
        }

        TestObject object = new TestObject(999L);
        List<TestObject> objects = Collections.singletonList(object);

        reset(unwrap(securedServiceSpy));
        assertEquals(objects, securedServiceSpy.doPreFilterByPermissionRead(objects));
        verify(unwrap(securedServiceSpy)).doPreFilterByPermissionRead(objects);

        setAuthentication("userRoleManager");
        assertEquals(objects, securedServiceSpy.doPreFilterByPermissionRead(new ArrayList<>(objects)));

        setAuthentication("userRoleOther");
        try {
            securedServiceSpy.doPreFilterByPermissionRead(objects);
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // Exception when we try to modify/filter immutable list
        }

        reset(unwrap(securedServiceSpy));
        List<TestObject> input = new ArrayList<>(objects);
        List<TestObject> result = securedServiceSpy.doPreFilterByPermissionRead(input);
        verify(unwrap(securedServiceSpy)).doPreFilterByPermissionRead(eq(result));
        verify(unwrap(securedServiceSpy)).doPreFilterByPermissionRead(same(input));
        assertTrue(result.isEmpty());

        Acl acl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Collections.singletonList(acl));
        assertEquals(objects, securedServiceSpy.doPreFilterByPermissionRead(new ArrayList<>(objects)));

        reset(aclServiceMock);
        // 'write' permission includes lower permission 'read'
        acl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.WRITE).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Collections.singletonList(acl));
        assertEquals(objects, securedServiceSpy.doPreFilterByPermissionRead(new ArrayList<>(objects)));
        // check findAclsWithAncestors was invoked while batch caching and findAclWithAncestors from evaluator
        verify(aclServiceMock).findAclsWithAncestors(eq(Collections.singletonList(object)));
        verify(aclServiceMock).findAclWithAncestors(object);

        setAuthentication("userRoleManager");
        mock(UserBuilder.create("userRoleManager").build());

        reset(aclServiceMock);
        // now manager have no privilege 'read' explicitly. it isn't inherited from manager's 'write'
        assertTrue(securedServiceSpy.doPreFilterByPermissionRead(new ArrayList<>(objects)).isEmpty());
    }

    @Test
    public void testPostFilterByPermissionRead() {
        setAuthentication("userRoleUser");
        try {
            List<Object> nonSecuredObjects = Collections.singletonList(new Object());
            securedServiceSpy.doPostFilterByPermissionRead(nonSecuredObjects);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ignored) {
            // only successors of SecuredObject are allowed
        }
        TestObject object = new TestObject(999L);
        List<TestObject> objects = Collections.singletonList(object);

        reset(unwrap(securedServiceSpy));
        assertEquals(objects, securedServiceSpy.doPostFilterByPermissionRead(objects));
        verify(unwrap(securedServiceSpy)).doPostFilterByPermissionRead(objects);

        setAuthentication("userRoleManager");
        assertEquals(objects, securedServiceSpy.doPreFilterByPermissionRead(new ArrayList<>(objects)));

        Acl acl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Collections.singletonList(acl));
        assertEquals(objects, securedServiceSpy.doPostFilterByPermissionRead(new ArrayList<>(objects)));

        reset(aclServiceMock);
        // 'write' permission includes lower permission 'read'
        acl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.WRITE).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Collections.singletonList(acl));
        assertEquals(objects, securedServiceSpy.doPostFilterByPermissionRead(new ArrayList<>(objects)));
        // check findAclsWithAncestors was invoked while batch caching and findAclWithAncestors from evaluator
        verify(aclServiceMock).findAclsWithAncestors(eq(Collections.singletonList(object)));
        verify(aclServiceMock).findAclWithAncestors(object);

        setAuthentication("userRoleManager");
        mock(UserBuilder.create("userRoleManager").build());

        reset(aclServiceMock);
        // now manager have no privilege 'read' explicitly. it isn't inherited from manager's 'write'
        assertEquals(0, securedServiceSpy.doPostFilterByPermissionRead(new ArrayList<>(objects)).size());
    }

    @Test
    public void testPostFilterByPermissionWriteArrays() {
        TestObject object = new TestObject(999L);
        TestObject[] objects = new TestObject[] {object};

        setAuthentication("userRoleUser");
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        setAuthentication("userRoleManager");
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        setAuthentication("userRoleOther");
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        Acl acl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Collections.singletonList(acl));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        reset(aclServiceMock);
        acl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.WRITE).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Collections.singletonList(acl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));
        // check findAclsWithAncestors was invoked while batch caching and findAclWithAncestors from evaluator
        verify(aclServiceMock).findAclsWithAncestors(eq(Collections.singletonList(object)));
        verify(aclServiceMock).findAclWithAncestors(object);
    }

    @Test
    public void testPreFilterByOwningAndPermissionHierarchy() {
        TestObject object = new TestObject(999L);
        TestObject[] objects = new TestObject[] {object};

        setAuthentication("userRoleOther");
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        Acl objAcl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Collections.singletonList(objAcl));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        objAcl = AclBuilder.create(1L, 999L).owner("userRoleOther").privilege("userRoleOther", Permission.READ).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Collections.singletonList(objAcl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        objAcl = AclBuilder.create(1L, 999L).owner("userRoleOther").build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Collections.singletonList(objAcl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        Acl parentAcl = AclBuilder.create(1L, 888L).owner("userRoleOther").build();
        objAcl = AclBuilder.create(1L, 999L).parent(parentAcl).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Arrays.asList(objAcl, parentAcl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        parentAcl = AclBuilder.create(1L, 888L).owner("userRoleOther").build();
        objAcl = AclBuilder.create(1L, 999L).parent(parentAcl).privilege("userRoleOther", Permission.READ).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Arrays.asList(objAcl, parentAcl));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        Acl parentAcl1 = AclBuilder.create(1L, 777L).owner("userRoleOther").build();
        Acl parentAcl2 = AclBuilder.create(1L, 888L).parent(parentAcl).privilege("userRoleOther", Permission.READ).build();
        objAcl = AclBuilder.create(1L, 999L).parent(parentAcl).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        parentAcl1 = AclBuilder.create(1L, 777L).owner("userRoleOther").build();
        parentAcl2 = AclBuilder.create(1L, 888L).parent(parentAcl).build();
        objAcl = AclBuilder.create(1L, 999L).parent(parentAcl).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        parentAcl = AclBuilder.create(1L, 888L).privilege("userRoleOther", Permission.WRITE).build();
        objAcl = AclBuilder.create(1L, 999L).parent(parentAcl).privilege("userRoleOther", Permission.READ).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Arrays.asList(objAcl, parentAcl));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        parentAcl = AclBuilder.create(1L, 888L).privilege("userRoleOther", Permission.WRITE).build();
        objAcl = AclBuilder.create(1L, 999L).parent(parentAcl).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Arrays.asList(objAcl, parentAcl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        parentAcl1 = AclBuilder.create(1L, 777L).privilege("userRoleOther", Permission.WRITE).build();
        parentAcl2 = AclBuilder.create(1L, 888L).parent(parentAcl).privilege("userRoleOther", Permission.WRITE).build();
        objAcl = AclBuilder.create(1L, 999L).parent(parentAcl).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        parentAcl1 = AclBuilder.create(1L, 777L).privilege("userRoleOther", Permission.WRITE).build();
        parentAcl2 = AclBuilder.create(1L, 888L).parent(parentAcl).privilege("userRoleOther", Permission.READ).build();
        objAcl = AclBuilder.create(1L, 999L).parent(parentAcl).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        parentAcl1 = AclBuilder.create(1L, 777L).privilege("userRoleOther", Permission.WRITE).build();
        parentAcl2 = AclBuilder.create(1L, 888L).parent(parentAcl).build();
        objAcl = AclBuilder.create(1L, 999L).parent(parentAcl).build();
        when(aclServiceMock.findAclWithAncestors(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));
    }

    //todo tests for @AclObjectFilter

    @Before
    public void initAuthData() {
        Role rootRole = RoleBuilder.create("Root").build();
        Role userRole = RoleBuilder.create("User", rootRole).privilege("TestObject", "READ").build();
        Role managerRole = RoleBuilder.create("Manager", userRole).privilege("TestObject", "WRITE").build();
        Role adminRole = RoleBuilder.create("Admin", userRole).privilege("TestObject", "WRITE").privilege("TestObject", "ADMIN").build();
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

    private void setAuthentication(String userName) {
        Authentication token = new UsernamePasswordAuthenticationToken(userName, userName);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    @SuppressWarnings("unchecked")
    private <T> T unwrap(T proxy) {
        try {
            Class<?> proxyClass = proxy.getClass();
            if (proxyClass.getName().toLowerCase().contains("cglib")) {
                Method targetSourceMethod = proxyClass.getDeclaredMethod("getTargetSource");
                targetSourceMethod.setAccessible(true);
                return (T) ((TargetSource) targetSourceMethod.invoke(proxy)).getTarget();
            } else if (AopUtils.isJdkDynamicProxy(proxy)) {
                return (T) ((Advised) proxy).getTargetSource().getTarget();
            }
            return proxy;
        } catch (Exception e) {
            throw new RuntimeException("Can't unwrap proxied object", e);
        }
    }

    public static class TestObject implements SecuredObject {

        private Long id;
        private String property;

        public TestObject(Long id) {
            this.id = id;
        }

        public TestObject(String property) {
            this.property = property;
        }

        @Override
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        @Override
        public String getObjectType() {
            return "TestObject";
        }
    }

}
