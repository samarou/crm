package com.itechart.sample.service.impl;

import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.model.persistent.security.Principal;
import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.security.ObjectIdentity;
import com.itechart.sample.model.security.Permission;
import com.itechart.sample.security.acl.AclCache;
import com.itechart.sample.service.AclService;
import com.itechart.sample.service.DictionaryService;
import com.itechart.sample.service.dao.AclDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author andrei.samarou
 */
@Service
public class AclServiceImpl implements AclService {

    @Autowired
    private AclDao aclDao;

    @Autowired
    private AclCache aclCache;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    @Transactional(readOnly = true)
    public Acl getAcl(Long aclId) {
        Acl acl = aclCache.get(aclId);
        if (acl == null) {
            acl = aclDao.get(aclId);
            aclCache.put(acl);
        }
        return acl;
    }

    @Override
    @Transactional(readOnly = true)
    public Acl getAcl(ObjectIdentity objectIdentity) {
        Acl acl = aclCache.get(objectIdentity);
        if (acl == null) {
            ObjectType objectType = dictionaryService.getObjectTypeByName(objectIdentity.getType());
            acl = aclDao.findByObjectIdentity(objectIdentity.getId(), objectType.getId());
            aclCache.put(acl);
        }
        return acl;
    }








    @Override
    @Transactional(readOnly = true)
    public List<Acl> findAcls(List<ObjectIdentity> objectIdentity) {
        return null;
    }

    @Override
    public List<Acl> findAclsWithAncestors(List<ObjectIdentity> objectIdentities) {
        return null;
    }

    @Override
    public List<Acl> findAclWithAncestors(ObjectIdentity objectIdentity) {
        return null;
    }







    @Override
    @Transactional
    public Acl createAcl(ObjectIdentity objectIdentity, ObjectIdentity parentIdentity, Principal owner, Set<Permission> permissions) {
        ObjectType objectType = dictionaryService.getObjectTypeByName(objectIdentity.getType());
        Acl acl = new Acl(objectType, (Long) objectIdentity.getId(), getAcl(parentIdentity), owner);
        aclCache.put(acl);
        return acl;
    }

    @Override
    @Transactional
    public void saveAcl(Acl acl) {
        aclDao.save(acl);
        aclCache.evict(acl.getId());
    }

    @Override
    @Transactional
    public void deleteAcl(Acl acl) {
        aclDao.delete(acl);
        List<Long> childIds = aclDao.findChildrenIds(acl.getId());
        for (Long childId : childIds) {
            aclCache.evict(childId);
        }
        aclCache.evict(acl.getId());
    }
}
