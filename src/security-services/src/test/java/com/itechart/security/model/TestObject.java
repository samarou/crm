package com.itechart.security.model;

import com.itechart.security.core.model.acl.SecuredObject;

/**
 * Test secured object
 *
 * @author andrei.samarou
 */
public class TestObject implements SecuredObject {

    private Long id;
    private String property;

    public TestObject(Long id) {
        this.id = id;
    }

    public TestObject(String property) {
        this.property = property;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String getObjectType() {
        return "TestObject";
    }
}
