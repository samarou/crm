package com.itechart.security.core.model.acl;

import java.io.Serializable;

/**
 * Contains attributes of object that distinguishes it from other objects.
 * Used for exactly determination of the object by set of attributes
 *
 * @author andrei.samarou
 */
public interface ObjectIdentity {

    Serializable getId();

    String getObjectType();

}