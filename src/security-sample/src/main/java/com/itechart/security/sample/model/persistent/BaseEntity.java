package com.itechart.security.sample.model.persistent;

import java.io.Serializable;

/**
 * Base class for all persistent entities
 *
 * @author andrei.samarou
 */
public abstract class BaseEntity implements Serializable {

    public abstract Serializable getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BaseEntity that = (BaseEntity) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{id=" + getId() + "}";
    }
}
