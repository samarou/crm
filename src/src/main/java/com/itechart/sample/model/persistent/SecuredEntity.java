package com.itechart.sample.model.persistent;

import com.itechart.sample.model.security.SecuredObject;

/**
 * Base class for persistent entities that support ACL security
 *
 * @author andrei.samarou
 */
public abstract class SecuredEntity extends BaseEntity implements SecuredObject {

    @Override
    public String getObjectType() {
        return getClass().getName();
    }
}