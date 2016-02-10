package com.itechart.security.service.impl;

import com.itechart.security.acl.AclCache;
import com.itechart.security.core.model.acl.ObjectIdentity;
import com.itechart.security.dao.AclDao;
import com.itechart.security.model.persistent.ObjectType;
import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.model.persistent.acl.AclObjectKey;
import com.itechart.security.service.AclService;
import com.itechart.security.service.ObjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author andrei.samarou
 */
@Service
public class AclServiceImpl implements AclService {

    public static final int DEFAULT_MAX_BATCH_SIZE = 10000;

    @Autowired
    private AclDao aclDao;

    @Autowired
    private AclCache aclCache;

    @Autowired
    private ObjectTypeService objectTypeService;

    private int maxBatchSize = DEFAULT_MAX_BATCH_SIZE;

    @Override
    @Transactional(readOnly = true)
    public Acl getAcl(Long aclId) {
        Acl acl = aclCache.get(aclId);
        if (acl == null) {
            acl = aclDao.get(aclId);
            if (acl == null) {
                throw new RuntimeException("ACL wasn't found by id: " + aclId);
            }
            aclCache.put(acl);
        }
        return acl;
    }

    @Override
    @Transactional(readOnly = true)
    public Acl getAcl(ObjectIdentity objectIdentity) {
        AclObjectKey objectKey = getObjectKey(objectIdentity);
        Acl acl = aclCache.get(objectKey);
        if (acl == null) {
            acl = aclDao.findByObjectKey(objectKey);
            if (acl == null) {
                // put to cache and return empty ACL,
                // to prevent repeating queries
                acl = new Acl(objectKey);
            }
            aclCache.put(acl);
        }
        return acl;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acl> findAcls(List<ObjectIdentity> objectIdentities) {
        if (CollectionUtils.isEmpty(objectIdentities)) {
            return Collections.emptyList();
        }
        if (objectIdentities.size() > maxBatchSize) {
            throw new RuntimeException("Count of object identities for retrieving ACL's exceeded "
                    + maxBatchSize + ": " + objectIdentities.size());
        }
        Collection<AclObjectKey> objectKeys = getObjectKeys(objectIdentities);
        List<Acl> result = new ArrayList<>(objectIdentities.size());
        Set<AclObjectKey> objectKeysForLoad = new HashSet<>();
        for (AclObjectKey objectKey : objectKeys) {
            Acl acl = aclCache.get(objectKey);
            if (acl == null) {
                objectKeysForLoad.add(objectKey);
            } else {
                result.add(acl);
            }
        }
        if (!objectKeysForLoad.isEmpty()) {
            List<Acl> loadedAcls = aclDao.findByObjectKeys(new ArrayList<>(objectKeysForLoad));
            for (Acl loadedAcl : loadedAcls) {
                objectKeysForLoad.remove(loadedAcl.getObjectKey());
                aclCache.put(loadedAcl);
            }
            result.addAll(loadedAcls);
            // for absent ACLs put to cache empty ACLs, to prevent repeating queries
            for (AclObjectKey objectKey : objectKeysForLoad) {
                Acl emptyAcl = new Acl(objectKey);
                aclCache.put(emptyAcl);
                result.add(emptyAcl);
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acl> findAclsWithAncestors(List<ObjectIdentity> objectIdentities) {
        List<Acl> result = findAcls(objectIdentities);
        List<Long> parentIdsForLoad = new ArrayList<>();
        List<Acl> loadedAcls = result;
        while (loadedAcls != null && !loadedAcls.isEmpty()) {
            // don't change to foreach because of concurrent modification
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0, size = loadedAcls.size(); i < size; i++) {
                Acl loadedAcl = loadedAcls.get(i);
                Long parentId = loadedAcl.getParentId();
                if (parentId != null && loadedAcl.isInheriting()) {
                    Acl cachedParent = aclCache.get(parentId);
                    if (cachedParent == null) {
                        parentIdsForLoad.add(parentId);
                    } else {
                        result.add(cachedParent);
                    }
                }
            }
            if (!parentIdsForLoad.isEmpty()) {
                loadedAcls = aclDao.findByIds(parentIdsForLoad);
                for (Acl loadedAcl : loadedAcls) {
                    aclCache.put(loadedAcl);
                }
                result.addAll(loadedAcls);
                parentIdsForLoad.clear();
            } else {
                loadedAcls = null;
            }
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Acl> findAclWithAncestors(ObjectIdentity objectIdentity) {
        return findAclsWithAncestors(Collections.singletonList(objectIdentity));
    }

    @Override
    @Transactional
    public Acl createAcl(ObjectIdentity objectIdentity, ObjectIdentity parentIdentity, Long ownerId) {
        Long parentId = null;
        if (parentIdentity != null) {
            Acl parentAcl = aclDao.findByObjectKey(getObjectKey(parentIdentity));
            if (parentAcl == null) {
                throw new RuntimeException("Parent ACL wasn't found for " + parentIdentity);
            }
            parentId = parentAcl.getId();
        }
        Acl acl = new Acl(getObjectKey(objectIdentity), parentId, ownerId);
        aclDao.save(acl);
        aclCache.put(acl);
        return acl;
    }

    @Override
    @Transactional
    public void updateAcl(Acl acl) {
        aclDao.update(acl);
        aclCache.evict(acl.getId());
    }

    @Override
    @Transactional
    public void deleteAcl(Acl acl) {
        aclDao.delete(acl);
        List<Long> childIds = aclDao.findChildrenIds(acl.getId());
        childIds.forEach(aclCache::evict);
        aclCache.evict(acl.getId());
    }

    private Collection<AclObjectKey> getObjectKeys(List<ObjectIdentity> objectIdentities) {
        ObjectType lastObjectType = null;
        List<AclObjectKey> objectKeys = new ArrayList<>(objectIdentities.size());
        for (ObjectIdentity objectIdentity : objectIdentities) {
            // assume that all (or most) identities has same type
            if (lastObjectType == null || !objectIdentity.getObjectType().equals(lastObjectType.getName())) {
                lastObjectType = objectTypeService.getObjectTypeByName(objectIdentity.getObjectType());
            }
            objectKeys.add(new AclObjectKey(lastObjectType.getId(), (Long) objectIdentity.getId()));
        }
        return objectKeys;
    }

    private AclObjectKey getObjectKey(ObjectIdentity objectIdentity) {
        ObjectType objectType = objectTypeService.getObjectTypeByName(objectIdentity.getObjectType());
        return new AclObjectKey(objectType.getId(), (Long) objectIdentity.getId());
    }

    public void setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
    }
}