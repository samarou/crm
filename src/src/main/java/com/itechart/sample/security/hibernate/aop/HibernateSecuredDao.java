package com.itechart.sample.security.hibernate.aop;

import org.hibernate.SessionFactory;

/**
 * Interface that provides access to Hibernate {@link SessionFactory}.
 * Can be used for support of security advices like {@link AclHibernateSecurityAspect}
 *
 * @author andrei.samarou
 * @see AclHibernateSecurityAspect
 */
public interface HibernateSecuredDao {

    /**
     * Method returns Hibernate {@link SessionFactory} that can be used
     * to create Hibernate Sessions
     */
    SessionFactory getSessionFactory();
}
