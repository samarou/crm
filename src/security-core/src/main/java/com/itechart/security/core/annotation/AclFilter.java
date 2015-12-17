package com.itechart.security.core.annotation;

import java.lang.annotation.*;

/**
 * Annotation for enabling a filtering SQL condition that applied to
 * returning object during fetch. SQL condition bases on ACL permissions
 * granted for object and authentificated principal.
 * <p>
 * If annotation is declared with empty body, then automatic type recognition
 * will be performed by analyzing return type of annotated method. In this case,
 * required permission will be set to {@link com.itechart.sample.model.security.Permission#READ}
 * and ACL inheritence won't be used. Subclasses of {@link com.itechart.sample.model.security.SecuredObject}
 * will have to be presented in method result type
 *
 * @author andrei.samarou
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AclFilter {

    /**
     * List of filter rules
     */
    AclFilterRule[] value() default {};
}