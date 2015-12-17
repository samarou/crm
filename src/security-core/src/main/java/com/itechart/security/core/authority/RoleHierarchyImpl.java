package com.itechart.security.core.authority;

import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.model.SecurityRole;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Provides cacheable role hierarchy loaded from repository
 *
 * @author andrei.samarou
 */
public class RoleHierarchyImpl implements RoleHierarchy {

    public static final String CACHE_KEY = "roleToParentsMap";

    private SecurityRepository securityRepository;

    private Ehcache roleHierarchyCache;

    @Override
    public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(
            Collection<? extends GrantedAuthority> authorities) {

        if (CollectionUtils.isEmpty(authorities)) {
            return Collections.emptyList();
        }
        Map<RoleAuthority, List<RoleAuthority>> roleToParentsMap = getRoleToParensMap();
        if (CollectionUtils.isEmpty(roleToParentsMap)) {
            return Collections.emptyList();
        }
        Set<GrantedAuthority> result = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof RoleAuthority) {
                result.add(authority);
                List<RoleAuthority> parentRoles = roleToParentsMap.get(authority);
                if (parentRoles != null) {
                    result.addAll(parentRoles);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Map<RoleAuthority, List<RoleAuthority>> getRoleToParensMap() {
        if (roleHierarchyCache != null) {
            Element element = roleHierarchyCache.get(CACHE_KEY);
            if (element != null) {
                return (Map<RoleAuthority, List<RoleAuthority>>) element.getObjectValue();
            }
        }
        List<SecurityRole> roles = securityRepository.getRoles();
        Map<Long, Object[]> roleMap = new HashMap<>(roles.size());
        for (SecurityRole role : roles) {
            Long parentId = role.getParent() != null ? role.getParent().getId() : null;
            roleMap.put(role.getId(), new Object[]{new RoleAuthority(role), parentId});
        }
        Map<RoleAuthority, List<RoleAuthority>> roleToParentsMap = new HashMap<>();
        for (Object[] pair : roleMap.values()) {
            Long parentId = (Long) pair[1];
            List<RoleAuthority> parents = parentId != null
                    ? new ArrayList<>() : Collections.emptyList();
            roleToParentsMap.put((RoleAuthority) pair[0], parents);
            while (parentId != null) {
                Object[] parentPair = roleMap.get(parentId);
                if (parentPair == null) {
                    throw new RuntimeException("Parent role is unreachable: " + parentId);
                }
                RoleAuthority parentRole = (RoleAuthority) parentPair[0];
                if (roleToParentsMap.containsKey(parentRole)) {
                    parents.addAll(roleToParentsMap.get(parentRole));
                    parents.add(parentRole);
                    break;
                } else {
                    parents.add(parentRole);
                    parentId = (Long) parentPair[1];
                }
            }
        }
        if (roleHierarchyCache != null) {
            roleHierarchyCache.put(new Element(CACHE_KEY, roleToParentsMap));
        }
        return roleToParentsMap;
    }

    @Required
    public void setSecurityRepository(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public void setRoleHierarchyCache(Ehcache roleHierarchyCache) {
        this.roleHierarchyCache = roleHierarchyCache;
    }
}
