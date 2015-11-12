package com.itechart.sample.security;

import com.itechart.sample.model.persistent.security.Role;
import com.itechart.sample.security.auth.RoleAuthority;
import com.itechart.sample.service.RoleService;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Provide cacheable role hierarchy loaded from DB
 *
 * @author andrei.samarou
 */
public class DaoRoleHierarchy implements RoleHierarchy {

    public static final String CACHE_KEY = "roleToParentsMap";

    @Autowired(required = true)
    private RoleService roleService;

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
        List<GrantedAuthority> result = new ArrayList<>();
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
    public Map<RoleAuthority, List<RoleAuthority>> getRoleToParensMap() {
        if (roleHierarchyCache != null) {
            Element element = roleHierarchyCache.get(CACHE_KEY);
            if (element != null) {
                return (Map<RoleAuthority, List<RoleAuthority>>) element.getObjectValue();
            }
        }
        List<Role> roles = roleService.getRoles();
        Map<Long, Object[]> roleMap = new HashMap<>(roles.size());
        for (Role role : roles) {
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
            roleHierarchyCache.put(new Element("roleToParentsMap", roleToParentsMap));
        }
        return roleToParentsMap;
    }

    public void setRoleHierarchyCache(Ehcache roleHierarchyCache) {
        this.roleHierarchyCache = roleHierarchyCache;
    }
}
