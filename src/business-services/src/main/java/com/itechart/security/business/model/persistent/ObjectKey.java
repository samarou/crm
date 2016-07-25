package com.itechart.security.business.model.persistent;

import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Represent complex key of business objects based on their id and type.
 *
 * @author yauheni.putsykovich
 */
@Embeddable
public class ObjectKey implements Serializable {

    @Column(name = "object_type_id", nullable = false, updatable = false)
    private Long objectTypeId;

    @Column(name = "object_id", nullable = false, updatable = false)
    private Long objectId;

    public ObjectKey() {
    }

    public ObjectKey(Long objectTypeId, Long objectId) {
        Assert.notNull(objectTypeId);
        Assert.notNull(objectId);
        this.objectTypeId = objectTypeId;
        this.objectId = objectId;
    }

    public Long getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(Long objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ObjectKey that = (ObjectKey) o;
        return objectTypeId.equals(that.objectTypeId) && objectId.equals(that.objectId);
    }

    @Override
    public int hashCode() {
        int result = objectTypeId.hashCode();
        result = 31 * result + objectId.hashCode();
        return result;
    }
}
