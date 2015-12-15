package com.itechart.sample.security;

import com.itechart.sample.model.persistent.security.Group;
import com.itechart.sample.model.persistent.security.Role;
import com.itechart.sample.model.persistent.security.User;
import com.itechart.sample.security.auth.GroupAuthority;
import com.itechart.sample.security.auth.RoleAuthority;
import com.itechart.sample.security.util.UserBuilder;
import com.itechart.sample.service.UserService;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

        User user = UserBuilder.create("user").group("group1", "group2")
                .role("Role").privilege("TestObject", "WRITE").privilege("TestObject", "CREATE")
                .role("Admin").privilege("TestObject", "ADMIN")
                .build();

        UserService userService = Mockito.mock(UserService.class);
        when(userService.findByName(user.getName())).thenReturn(user);

        UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setUserService(userService);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());

        assertThat(userDetails, instanceOf(com.itechart.sample.model.security.User.class));

        assertEquals(user.getId(), ((com.itechart.sample.model.security.User) userDetails).getUserId());
        assertEquals(user.getUserName(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(4, authorities.size());

        Collection<GrantedAuthority> expectedAuthorities = new HashSet<>();
        for (Group group : user.getGroups()) {
            expectedAuthorities.add(new GroupAuthority(group));
        }
        for (Role role : user.getRoles()) {
            expectedAuthorities.add(new RoleAuthority(role));
        }

        assertEquals(expectedAuthorities, authorities);

        user.setActive(false);
        userDetails = userDetailsService.loadUserByUsername(user.getName());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertFalse(userDetails.isEnabled());
    }
}