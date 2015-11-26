package com.itechart.sample.security.annotation;

import com.itechart.sample.model.security.Permission;
import com.itechart.sample.model.security.SecuredObject;

import java.lang.annotation.*;

/**
 * Rule describes required ACL constraints
 *
 * @author andrei.samarou
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AclRule {

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