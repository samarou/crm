package com.itechart.security.web.controller;

import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.core.SecurityUtils;
import com.itechart.security.core.acl.AclPermissionEvaluator;
import com.itechart.security.core.model.acl.ObjectIdentity;
import com.itechart.security.core.model.acl.ObjectIdentityImpl;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.model.dto.AclEntryDto;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.service.AclService;
import com.itechart.security.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.itechart.common.model.util.CollectionConverter.convertCollection;
import static com.itechart.security.model.util.AclConverter.convert;

public abstract class SecuredController {

    @Autowired
    private AclService aclService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private AclPermissionEvaluator aclPermissionEvaluator;

    public boolean isAllowed(Long entityId, String value) {
        Permission permission = Permission.valueOf(value.toUpperCase());
        return aclPermissionEvaluator.hasPermission(SecurityUtils.getAuthentication(), createIdentity(entityId), permission);
    }

    protected void createAcl(Long entityId) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        aclService.createAcl(createIdentity(entityId), null, userId);
    }

    protected void deleteAcl(Long entityId) {
        Acl acl = getAcl(entityId);
        aclService.deleteAcl(acl);
    }

    protected List<AclEntryDto> getAcls(Long entityId) {
        Acl acl = getAcl(entityId);
        Map<Long, Set<Permission>> allPermissions = acl.getPermissions();
        List<Principal> principals = principalService.getByIds(new ArrayList<>(allPermissions.keySet()));
        return convertCollection(principals, principal -> convert(principal, allPermissions.get(principal.getId())));
    }

    protected void createOrUpdateAcls(Long entityId, List<AclEntryDto> aclEntries) {
        Acl acl = getAcl(entityId);
        aclEntries.forEach(aclEntry -> acl.setPermissions(aclEntry.getPrincipalId(), convert(aclEntry)));
        aclService.updateAcl(acl);
    }

    protected void deleteAcl(Long entityId, Long principalId) {
        Acl acl = getAcl(entityId);
        acl.removePrincipal(principalId);
        aclService.updateAcl(acl);
    }

    private Acl getAcl(Long entityId) {
        return aclService.getAcl(createIdentity(entityId));
    }

    private ObjectIdentity createIdentity(Long entityId) {
        return new ObjectIdentityImpl(entityId, getObjectType().getName());
    }

    public abstract ObjectTypes getObjectType();
}
