package com.itechart.sample.security;

import com.itechart.sample.model.persistent.security.Group;
import com.itechart.sample.model.persistent.security.Role;
import com.itechart.sample.security.auth.GroupAuthority;
import com.itechart.sample.security.auth.RoleAuthority;
import com.itechart.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

/**
 * Service loads user-specific data for authentication
 *
 * @author andrei.samarou
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired(required = true)
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.itechart.sample.model.persistent.security.User user = userService.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " was not found");
        }
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                authorities.add(new RoleAuthority(role));
            }
        }
        Set<Group> groups = user.getGroups();
        if (groups != null && groups.size() > 0) {
            for (Group group : groups) {
                authorities.add(new GroupAuthority(group));
            }
        }
        return new User(username, user.getPassword(), user.isActive(), true, true, true, authorities);
    }
}