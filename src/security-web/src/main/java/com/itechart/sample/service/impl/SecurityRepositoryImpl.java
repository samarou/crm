package com.itechart.sample.service.impl;

import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.service.AclService;
import com.itechart.sample.service.DictionaryService;
import com.itechart.sample.service.RoleService;
import com.itechart.sample.service.UserService;
import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.core.model.acl.ObjectIdentity;
import com.itechart.security.core.model.acl.SecurityAcl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Implementation of {@link SecurityRepository} as a facade,
 * that delegates calls to set of services
 *
 * @author andrei.samarou
 */
public class SecurityRepositoryImpl implements SecurityRepository {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AclService aclService;
    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public SecurityUser findUserByName(String userName) {
        return userService.findByName(userName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SecurityRole> getRoles() {
        return (List) roleService.getRoles();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SecurityAcl> findAcls(ObjectIdentity oid) {
        return (List) aclService.findAclWithAncestors(oid);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SecurityAcl> findAcls(List<ObjectIdentity> oids) {
        return (List) aclService.findAclsWithAncestors(oids);
    }

    @Override
    public Long getObjectTypeIdByName(String objectTypeName) {
        ObjectType objectType = dictionaryService.getObjectTypeByName(objectTypeName);
        return objectType != null ? objectType.getId() : null;
    }
}
