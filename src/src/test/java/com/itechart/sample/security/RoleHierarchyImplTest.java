package com.itechart.sample.security;

import com.itechart.sample.model.persistent.security.Role;
import com.itechart.sample.security.auth.RoleAuthority;
import com.itechart.sample.security.util.RoleBuilder;
import com.itechart.sample.service.RoleService;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author andrei.samarou
 */
public class RoleHierarchyImplTest {

    @Test
    public void testGetReachableGrantedAuthorities() {
        RoleService roleService = mock(RoleService.class);
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setRoleService(roleService);

        Role rootRole = RoleBuilder.create("Root").build();
        Role userRole = RoleBuilder.create("User", rootRole).privilege("TestObject", "READ").build();
        Role managerRole = RoleBuilder.create("Manager", userRole).privilege("TestObject", "WRITE").build();
        when(roleService.getRoles()).thenReturn(Arrays.asList(rootRole, userRole, managerRole));

        List<RoleAuthority> userAuthorities = Collections.singletonList(new RoleAuthority(managerRole));
        Collection<? extends GrantedAuthority> authorities = roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
        assertTrue(authorities.containsAll(Arrays.asList(new RoleAuthority(managerRole), new RoleAuthority(userRole), new RoleAuthority(rootRole))));
        assertEquals(3, authorities.size());

        userAuthorities = Arrays.asList(new RoleAuthority(rootRole), new RoleAuthority(userRole));
        authorities = roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
        assertTrue(authorities.containsAll(Arrays.asList(new RoleAuthority(rootRole), new RoleAuthority(userRole))));
        assertEquals(2, authorities.size());

        userAuthorities = Collections.singletonList(new RoleAuthority(rootRole));
        authorities = roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
        assertEquals(Collections.singleton(new RoleAuthority(rootRole)), authorities);

        Ehcache ehcache = mock(Ehcache.class);
        Map<RoleAuthority, List<RoleAuthority>> rolesMap = Collections.singletonMap(new RoleAuthority(userRole),
                Collections.singletonList(new RoleAuthority(rootRole)));
        when(ehcache.get(RoleHierarchyImpl.CACHE_KEY)).thenReturn(new Element(RoleHierarchyImpl.CACHE_KEY, rolesMap));
        roleHierarchy.setRoleHierarchyCache(ehcache);

        reset(roleService);
        userAuthorities = Collections.singletonList(new RoleAuthority(userRole));
        authorities = roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
        assertTrue(authorities.containsAll(Arrays.asList(new RoleAuthority(userRole), new RoleAuthority(rootRole))));
        assertEquals(2, authorities.size());
        verifyNoMoreInteractions(roleService);
    }
}
