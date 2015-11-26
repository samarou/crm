package com.itechart.sample.security.hibernate.filter;

/**
 * Enumeration of filter types
 *
 * @author andrei.samarou
 */
public enum FilterType {
    ACL_PLAIN,     // check access for first level acl
    ACL_HIERARCHY  // considers acl objects hierarchy
}
