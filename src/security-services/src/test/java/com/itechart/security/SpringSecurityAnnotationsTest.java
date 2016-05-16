package com.itechart.security;

import com.itechart.security.model.TestObject;
import com.itechart.security.service.SecuredService;
import com.itechart.security.service.AclService;
import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.core.model.acl.ObjectIdentityImpl;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.model.acl.SecurityAcl;
import com.itechart.security.core.test.AclBuilder;
import com.itechart.security.core.test.RoleBuilder;
import com.itechart.security.core.test.UserBuilder;
import com.itechart.security.core.test.auth.WithUser;
import com.itechart.security.core.test.junit.ContextAwareJUnit4ClassRunner;
import com.itechart.security.core.test.model.SecurityRoleImpl;
import org.junit.After;
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;

import static com.itechart.security.core.test.util.ThrowableAssert.assertThrown;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit for testing spring security annotations
 * that can be applied to business service
 *
 * @author andrei.samarou
 * @see org.springframework.security.access.prepost.PreAuthorize
 * @see org.springframework.security.access.prepost.PreFilter
 * @see org.springframework.security.access.prepost.PostFilter
 */
@RunWith(ContextAwareJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:securityServicesTestContext.xml")
public class SpringSecurityAnnotationsTest {

    @Autowired
    private SecurityRepository securityRepositoryMock;
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
    @Test(expected = AccessDeniedException.class)
    public void testHasPermission11() {
    }

    @WithUser("userRoleManager")
    @PreAuthorize("hasPermission(999, 'TestObject', 'ADMIN')")
    @Test(expected = AccessDeniedException.class)
    public void testHasPermission12() {
    }

    @WithUser("userRoleOther")
    @PreAuthorize("hasPermission(999, 'OtherObject', 'Read')")
    @Test(expected = IllegalArgumentException.class)
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
        assertThrown(AccessDeniedException.class, () -> securedService.doPreAuthorizeByReadObjectId(999L));

        SecurityAcl acl = AclBuilder.create(1L, 999L).privilege("userRoleManager", Permission.READ).build();
        when(securityRepositoryMock.findAcls(new ObjectIdentityImpl(999L, "TestObject")))
                .thenReturn(Collections.singletonList(acl));
        securedService.doPreAuthorizeByReadObjectId(999L);

        setAuthentication("userRoleOther");
        assertThrown(AccessDeniedException.class, () -> securedService.doPreAuthorizeByReadObjectId(999L));

        setAuthentication("userRoleManager");
        acl = AclBuilder.create(1L, 999L).privilege("userRoleManager", Permission.READ).build();
        when(securityRepositoryMock.findAcls(new ObjectIdentityImpl(999L, "TestObject")))
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

        assertThrown(UnsupportedOperationException.class,
                () -> securedServiceSpy.doPreFilterWithObjectPropertyEqUserName(objects));
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

        List<Object> nonSecuredObjects = Collections.singletonList(new Object());
        assertThrown(IllegalArgumentException.class,
                () -> securedServiceSpy.doPreFilterByPermissionRead(nonSecuredObjects));

        TestObject object = new TestObject(999L);
        List<TestObject> objects = new LinkedList<>(Collections.singletonList(object));

        reset(unwrap(securedServiceSpy));
        assertEquals(0, securedServiceSpy.doPreFilterByPermissionRead(objects).size());
        verify(unwrap(securedServiceSpy)).doPreFilterByPermissionRead(objects);

        setAuthentication("userRoleManager");
        objects = new LinkedList<>(Collections.singletonList(object));
        assertEquals(0, securedServiceSpy.doPreFilterByPermissionRead(new ArrayList<>(objects)).size());

        setAuthentication("userRoleOther");
        assertThrown(UnsupportedOperationException.class,
                () -> securedServiceSpy.doPreFilterByPermissionRead(Collections.singletonList(object)));
    }

    @Test
    public void testPostFilterByPermissionRead() {
        setAuthentication("userRoleUser");

        List<Object> nonSecuredObjects = Collections.singletonList(new Object());
        assertThrown(IllegalArgumentException.class,
                () -> securedServiceSpy.doPostFilterByPermissionRead(nonSecuredObjects));

        TestObject object = new TestObject(999L);
        List<TestObject> objects = Collections.singletonList(object);

        reset(unwrap(securedServiceSpy));
        assertEquals(0, securedServiceSpy.doPostFilterByPermissionRead(objects).size());
        verify(unwrap(securedServiceSpy)).doPostFilterByPermissionRead(objects);

        setAuthentication("userRoleManager");
        SecurityAcl acl = AclBuilder.create(1L, 999L).privilege("userRoleManager", Permission.READ).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Collections.singletonList(acl));
        assertEquals(objects, securedServiceSpy.doPreFilterByPermissionRead(new ArrayList<>(objects)));

        setAuthentication("userRoleOther");
        acl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Collections.singletonList(acl));
        assertEquals(0, securedServiceSpy.doPostFilterByPermissionRead(new ArrayList<>(objects)).size());

        reset(aclServiceMock);
        reset(securityRepositoryMock);
        setAuthentication("userRoleManager");
        initAuthData();
        // 'write' permission includes lower permission 'read'
        acl = AclBuilder.create(1L, 999L).privilege("userRoleManager", Permission.WRITE).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Collections.singletonList(acl));
        assertEquals(objects, securedServiceSpy.doPostFilterByPermissionRead(new ArrayList<>(objects)));
        // check findAcls was invoked while batch caching and findAcls from evaluator
        verify(aclServiceMock).findAclsWithAncestors(eq(Collections.singletonList(object)));
        verify(securityRepositoryMock).findAcls(object);

        setAuthentication("userRoleManager");
        mock(UserBuilder.create("userRoleManager").build());

        reset(aclServiceMock);
        // now manager have no privilege 'read' explicitly. it isn't inherited from manager's 'write'
        assertEquals(0, securedServiceSpy.doPostFilterByPermissionRead(new ArrayList<>(objects)).size());
    }

    @Test
    public void testPostFilterByPermissionWriteArrays() {
        TestObject object = new TestObject(999L);
        TestObject[] objects = new TestObject[]{object};

        setAuthentication("userRoleUser");
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        setAuthentication("userRoleManager");
        SecurityAcl acl = AclBuilder.create(1L, 999L).privilege("userRoleManager", Permission.WRITE).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Collections.singletonList(acl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        setAuthentication("userRoleOther");
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        acl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Collections.singletonList(acl));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        reset(aclServiceMock);
        reset(securityRepositoryMock);
        initAuthData();
        setAuthentication("userRoleManager");
        acl = AclBuilder.create(1L, 999L).privilege("userRoleManager", Permission.WRITE).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Collections.singletonList(acl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));
        // check findAcls was invoked while batch caching and findAcls from evaluator
        verify(aclServiceMock).findAclsWithAncestors(eq(Collections.singletonList(object)));
        verify(securityRepositoryMock).findAcls(object);
    }

    @Test
    public void testPreFilterByOwningAndPermissionHierarchy() {
        TestObject object = new TestObject(999L);
        TestObject[] objects = new TestObject[]{object};

        setAuthentication("userRoleOther");
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        SecurityAcl objAcl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Collections.singletonList(objAcl));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        setAuthentication("userRoleManager");
        objAcl = AclBuilder.create(1L, 999L).owner("userRoleManager").privilege("userRoleManager", Permission.READ).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Collections.singletonList(objAcl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        objAcl = AclBuilder.create(1L, 999L).owner("userRoleManager").build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Collections.singletonList(objAcl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        SecurityAcl parentAcl = AclBuilder.create(1L, 888L).owner("userRoleManager").build();
        objAcl = AclBuilder.create(1L, 999L).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Arrays.asList(objAcl, parentAcl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        setAuthentication("userRoleOther");
        parentAcl = AclBuilder.create(1L, 888L).owner("userRoleOther").build();
        objAcl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Arrays.asList(objAcl, parentAcl));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        SecurityAcl parentAcl1 = AclBuilder.create(1L, 777L).owner("userRoleOther").build();
        SecurityAcl parentAcl2 = AclBuilder.create(1L, 888L).privilege("userRoleOther", Permission.READ).build();
        objAcl = AclBuilder.create(1L, 999L).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        setAuthentication("userRoleManager");
        parentAcl1 = AclBuilder.create(1L, 777L).owner("userRoleManager").build();
        parentAcl2 = AclBuilder.create(1L, 888L).build();
        objAcl = AclBuilder.create(1L, 999L).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        setAuthentication("userRoleOther");
        parentAcl = AclBuilder.create(1L, 888L).privilege("userRoleOther", Permission.WRITE).build();
        objAcl = AclBuilder.create(1L, 999L).privilege("userRoleOther", Permission.READ).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Arrays.asList(objAcl, parentAcl));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        setAuthentication("userRoleManager");
        parentAcl = AclBuilder.create(1L, 888L).privilege("userRoleManager", Permission.WRITE).build();
        objAcl = AclBuilder.create(1L, 999L).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Arrays.asList(objAcl, parentAcl));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        parentAcl1 = AclBuilder.create(1L, 777L).privilege("userRoleManager", Permission.WRITE).build();
        parentAcl2 = AclBuilder.create(1L, 888L).privilege("userRoleManager", Permission.WRITE).build();
        objAcl = AclBuilder.create(1L, 999L).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));

        setAuthentication("userRoleOther");
        parentAcl1 = AclBuilder.create(1L, 777L).privilege("userRoleOther", Permission.WRITE).build();
        parentAcl2 = AclBuilder.create(1L, 888L).privilege("userRoleOther", Permission.READ).build();
        objAcl = AclBuilder.create(1L, 999L).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertEquals(0, securedService.doPostFilterByPermissionWrite(objects).length);

        setAuthentication("userRoleManager");
        parentAcl1 = AclBuilder.create(1L, 777L).privilege("userRoleManager", Permission.WRITE).build();
        parentAcl2 = AclBuilder.create(1L, 888L).build();
        objAcl = AclBuilder.create(1L, 999L).build();
        when(securityRepositoryMock.findAcls(object)).thenReturn(Arrays.asList(objAcl, parentAcl1, parentAcl2));
        assertArrayEquals(objects, securedService.doPostFilterByPermissionWrite(objects));
    }

    @PostConstruct
    public void initAuthData() {
        SecurityRoleImpl rootRole = RoleBuilder.create("Root").build();
        SecurityRoleImpl userRole = RoleBuilder.create("User", rootRole).privilege("TestObject", "READ").build();
        SecurityRoleImpl managerRole = RoleBuilder.create("Manager", userRole).privilege("TestObject", "WRITE").build();
        SecurityRoleImpl adminRole = RoleBuilder.create("Admin", userRole).privilege("TestObject", "WRITE").privilege("TestObject", "ADMIN").build();
        mock(rootRole, userRole, managerRole, adminRole);

        mock(UserBuilder.create("userEmpty").build());
        mock(UserBuilder.create("userRoleUser").role(userRole).build());
        mock(UserBuilder.create("userRoleManager").role(managerRole).build());
        mock(UserBuilder.create("userRoleAdmin").role(adminRole).build());
        mock(UserBuilder.create("userRoleOther").role("Other").privilege("OtherObject", "Read").build());
    }

    @After
    public void clean() {
        SecurityContextHolder.clearContext();
    }

    private void mock(SecurityUser user) {
        when(securityRepositoryMock.findUser(user.getUserName())).thenReturn(user);
    }

    private void mock(SecurityRole... role) {
        when(securityRepositoryMock.getRoles()).thenReturn(Arrays.asList(role));
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
}