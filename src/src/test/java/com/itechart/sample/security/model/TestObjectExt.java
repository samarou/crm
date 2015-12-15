package com.itechart.sample.security.model;

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
