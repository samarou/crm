package com.itechart.security.business.model.persistent;

import com.itechart.common.model.persistent.BaseEntity;
import com.itechart.security.core.model.acl.SecuredObject;
import lombok.EqualsAndHashCode;

/**
 * Base class for persistent entities that support ACL security
 *
 * @author andrei.samarou
 */
@EqualsAndHashCode(callSuper = true)
public abstract class SecuredEntity extends BaseEntity implements SecuredObject {

    @Override
    public String getObjectType() {
        return getClass().getName();
    }
}