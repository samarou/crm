package com.itechart.security.hibernate.model;

import javax.persistence.Entity;

/**
 * @author andrei.samarou
 */
@Entity
public class TestObjectExt extends TestObject {
    @Override
    public String getObjectType() {
        return "TestObjectExt";
    }
}
