package com.itechart.security.core.authority;

import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.core.test.RoleBuilder;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author andrei.samarou
 */
public class RoleHierarchyImplTest {

    @Test
    public void testGetReachableGrantedAuthorities() {
        SecurityRepository securityRepository = mock(SecurityRepository.class);
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setSecurityRepository(securityRepository);

        SecurityRole rootRole = RoleBuilder.create("Root").build();
        SecurityRole userRole = RoleBuilder.create("User", rootRole).privilege("TestObject", "READ").build();
        SecurityRole managerRole = RoleBuilder.create("Manager", userRole).privilege("TestObject", "WRITE").build();
        when(securityRepository.getRoles()).thenReturn(Arrays.asList(rootRole, userRole, managerRole));

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

        Mockito.reset(securityRepository);
        userAuthorities = Collections.singletonList(new RoleAuthority(userRole));
        authorities = roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
        assertTrue(authorities.containsAll(Arrays.asList(new RoleAuthority(userRole), new RoleAuthority(rootRole))));
        assertEquals(2, authorities.size());
        Mockito.verifyNoMoreInteractions(securityRepository);
    }
}
