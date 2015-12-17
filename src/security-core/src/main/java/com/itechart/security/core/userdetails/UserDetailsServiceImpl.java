package com.itechart.security.core.userdetails;

import com.itechart.security.core.authority.GroupAuthority;
import com.itechart.security.core.authority.RoleAuthority;
import com.itechart.security.core.model.SecurityGroup;
import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.core.SecurityRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Service loads user-specific data for authentication
 *
 * @author andrei.samarou
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private SecurityRepository securityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser user = securityRepository.findUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " was not found");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<? extends SecurityRole> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {
            for (SecurityRole role : roles) {
                authorities.add(new RoleAuthority(role));
            }
        }
        Set<? extends SecurityGroup> groups = user.getGroups();
        if (groups != null && groups.size() > 0) {
            for (SecurityGroup group : groups) {
                authorities.add(new GroupAuthority(group));
            }
        }
        return new UserDetailsImpl(user.getId(), username, user.getPassword(), user.isActive(), authorities);
    }

    @Required
    public void setSecurityRepository(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }
}