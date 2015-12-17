package com.itechart.security.core.model.acl;

import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Basic implementation of the {@link ObjectIdentity}
 *
 * @author andrei.samarou
 */
public class ObjectIdentityImpl implements ObjectIdentity {

    private Serializable id;
    private String type;

    public ObjectIdentityImpl(Serializable id, String type) {
        Assert.notNull(id, "id required");
        Assert.notNull(type, "type required");
        this.id = id;
        this.type = type;
    }

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public String getObjectType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ObjectIdentityImpl that = (ObjectIdentityImpl) o;
        return id.equals(that.id) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ObjectIdentityImpl{id=" + id + ", type='" + type + "'}";
    }
}
