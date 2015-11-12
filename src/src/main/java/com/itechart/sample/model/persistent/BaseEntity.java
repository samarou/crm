package com.itechart.sample.model.persistent;

import com.itechart.sample.security.acl.SecuredObject;

import java.io.Serializable;

/**
 * Base class for all persistent entities
 *
 * @author andrei.samarou
 */
public abstract class BaseEntity implements SecuredObject, Serializable {

    public abstract Serializable getId();

    public String getType() {
        return getClass().getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseEntity that = (BaseEntity) o;

        if (getId() == null || !getId().equals(that.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }
}
