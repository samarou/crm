package com.itechart.security.core.annotation;

import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.model.acl.SecuredObject;

import java.lang.annotation.*;

/**
 * Rule describes required ACL constraints.
 * For details about permission's inheritance see implementations
 * of ACL filtering
 *
 * @author andrei.samarou
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AclFilter.class)
@Documented
public @interface AclFilterRule {

    /**
     * Secured object types
     */
    Class<? extends SecuredObject>[] type();

    /**
     * List of required permissions
     */
    Permission[] permissions() default {Permission.READ};

    /**
     * Inherit permissions from parent objects
     */
    boolean inherit() default false;

}