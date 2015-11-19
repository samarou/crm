package com.itechart.sample.model.persistent;

import com.itechart.sample.model.security.ObjectIdentity;

/**
 * Base class for persistent entities that support ACL security
 *
 * @author andrei.samarou
 */
public abstract class SecuredEntity extends BaseEntity implements ObjectIdentity {

    @Override
    public String getObjectType() {
        //todo прописать привязку типа объекта к классу в ObjectTypes?
        //todo прописать id типов объектов в ObjectType чтобы не лезть за ними в БД?
        return getClass().getName();
    }
}