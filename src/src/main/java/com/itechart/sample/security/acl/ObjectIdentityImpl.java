package com.itechart.sample.security.acl;

import org.springframework.security.acls.domain.IdentityUnavailableException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Represents the ACL identity of an individual domain object instance
 *
 * @author andrei.samarou
 */
public class ObjectIdentityImpl implements ObjectIdentity {

    private Serializable identifier;
    private String type;

    public ObjectIdentityImpl(String type, Serializable identifier) {
        Assert.hasText(type, "type required");
        Assert.notNull(identifier, "identifier required");
        this.identifier = identifier;
        this.type = type;
    }

    public ObjectIdentityImpl(Object object) throws IdentityUnavailableException {
        Assert.isInstanceOf(SecuredObject.class, object, "object is not instance of " + SecuredObject.class);
        SecuredObject securedObject = (SecuredObject) object;
        identifier = securedObject.getId();
        type = securedObject.getType();
    }

    @Override
    public Serializable getIdentifier() {
        return identifier;
    }

    @Override
    public String getType() {
        return type;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ObjectIdentityImpl)) {
            return false;
        }
        ObjectIdentityImpl other = (ObjectIdentityImpl) o;
        return identifier.equals(other.identifier) && type.equals(other.type);
    }

    public int hashCode() {
        int code = 31;
        code ^= this.type.hashCode();
        code ^= this.identifier.hashCode();
        return code;
    }
}
