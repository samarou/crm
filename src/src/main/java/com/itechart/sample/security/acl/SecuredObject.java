package com.itechart.sample.security.acl;

import java.io.Serializable;

/**
 * Base interface for all domain objects that supported ACL security
 *
 * @author andrei.samarou
 */
public interface SecuredObject {

    Serializable getId();

    String getType();

}
