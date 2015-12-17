package com.itechart.security.core.userdetails;

import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.authority.GroupAuthority;
import com.itechart.security.core.authority.RoleAuthority;
import com.itechart.security.core.model.SecurityGroup;
import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.core.test.UserBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author andrei.samarou
 */
public class UserDetailsServiceImplTest {

    @Test
    public void testLoadUserByUsername() {

        SecurityUser user = UserBuilder.create("user").group("group1", "group2")
                .role("Role").privilege("TestObject", "WRITE").privilege("TestObject", "CREATE")
                .role("Admin").privilege("TestObject", "ADMIN")
                .build();

        SecurityRepository securityRepository = Mockito.mock(SecurityRepository.class);
        when(securityRepository.findUserByName(user.getUserName())).thenReturn(user);

        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setSecurityRepository(securityRepository);
        org.springframework.security.core.userdetails.UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getUserName());
        assertThat(userDetails, instanceOf(UserDetails.class));

        assertEquals(user.getId(), ((UserDetails) userDetails).getUserId());
        Assert.assertEquals(user.getUserName(), userDetails.getUsername());
        Assert.assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(4, authorities.size());

        Collection<GrantedAuthority> expectedAuthorities = new HashSet<>();
        for (SecurityGroup group : user.getGroups()) {
            expectedAuthorities.add(new GroupAuthority(group));
        }
        for (SecurityRole role : user.getRoles()) {
            expectedAuthorities.add(new RoleAuthority(role));
        }

        assertEquals(expectedAuthorities, authorities);

        user = UserBuilder.create("user", false).build();
        when(securityRepository.findUserByName(user.getUserName())).thenReturn(user);

        userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertFalse(userDetails.isEnabled());
    }
}