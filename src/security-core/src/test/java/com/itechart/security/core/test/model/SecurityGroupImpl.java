package com.itechart.security.core.test.model;

import com.itechart.security.core.model.SecurityGroup;

import java.io.Serializable;

/**
 * Implementation of {@link SecurityGroup} for test purposes
 *
 * @author andrei.samarou
 */
public class SecurityGroupImpl implements SecurityGroup, Serializable {

    private String name;

    public SecurityGroupImpl(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return (long) name.hashCode();
    }

    @Override
    public String getName() {
        return name;
    }
}
