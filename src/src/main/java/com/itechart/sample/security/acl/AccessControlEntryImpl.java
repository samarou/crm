package com.itechart.sample.security.acl;

import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Default implementation of <code>AccessControlEntry</code>
 *
 * @author andrei.samarou
 */
public class AccessControlEntryImpl implements AccessControlEntry {

    private Serializable id;
    private Permission permission;
    private Sid sid;
    private boolean granting;

    private Acl acl;

    // ~ Constructors
    // ===================================================================================================

    public AccessControlEntryImpl( Serializable id, Acl acl, Sid sid, Permission permission, boolean granting) {
        Assert.notNull(acl, "Acl required");
        Assert.notNull(sid, "Sid required");
        Assert.notNull(permission, "Permission required");

        this.id = id;
        this.acl = acl; // can be null
        this.sid = sid;
        this.permission = permission;
        this.granting = granting;

    }

    // ~ Methods
    // ========================================================================================================

    public boolean equals(Object arg0) {
        if (!(arg0 instanceof AccessControlEntryImpl)) {
            return false;
        }

        AccessControlEntryImpl rhs = (AccessControlEntryImpl) arg0;

        if (this.acl == null) {
            if (rhs.getAcl() != null) {
                return false;
            }
            // Both this.acl and rhs.acl are null and thus equal
        }
        else {
            // this.acl is non-null
            if (rhs.getAcl() == null) {
                return false;
            }

            // Both this.acl and rhs.acl are non-null, so do a comparison
            if (this.acl.getObjectIdentity() == null) {
                if (rhs.acl.getObjectIdentity() != null) {
                    return false;
                }
                // Both this.acl and rhs.acl are null and thus equal
            }
            else {
                // Both this.acl.objectIdentity and rhs.acl.objectIdentity are non-null
                if (!this.acl.getObjectIdentity()
                        .equals(rhs.getAcl().getObjectIdentity())) {
                    return false;
                }
            }
        }

        if (this.id == null) {
            if (rhs.id != null) {
                return false;
            }
            // Both this.id and rhs.id are null and thus equal
        }
        else {
            // this.id is non-null
            if (rhs.id == null) {
                return false;
            }

            // Both this.id and rhs.id are non-null
            if (!this.id.equals(rhs.id)) {
                return false;
            }
        }

        if (
                (this.granting != rhs.isGranting())
                || !this.permission.equals(rhs.getPermission())
                || !this.sid.equals(rhs.getSid())) {
            return false;
        }

        return true;
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public Sid getSid() {
        return sid;
    }

    @Override
    public boolean isGranting() {
        return granting;
    }

    @Override
    public Acl getAcl() {
        return acl;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AccessControlEntryImpl[");
        sb.append("id: ").append(this.id).append("; ");
        sb.append("granting: ").append(this.granting).append("; ");
        sb.append("sid: ").append(this.sid).append("; ");
        sb.append("permission: ").append(this.permission).append("; ");
        sb.append("]");

        return sb.toString();
    }
}
