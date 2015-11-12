package com.itechart.sample.security.acl;

import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;

import java.io.Serializable;

/**
 * Strategy that provides the ability to determine which {@link ObjectIdentity}
 * will be returned for a particular domain object
 *
 * @author andrei.samarou
 */
public class ObjectIdentityRetrievalStrategyImpl implements ObjectIdentityRetrievalStrategy, ObjectIdentityGenerator {

    public ObjectIdentity getObjectIdentity(Object domainObject) {
        return new ObjectIdentityImpl(domainObject);
    }

    public ObjectIdentity createObjectIdentity(Serializable id, String type) {
        return new ObjectIdentityImpl(type, id);
    }
}
