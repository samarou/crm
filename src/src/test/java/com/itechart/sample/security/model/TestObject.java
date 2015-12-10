package com.itechart.sample.security.model;

import com.itechart.sample.model.security.SecuredObject;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Test secured entity
 *
 * @author andrei.samarou
 */
@Entity
public class TestObject implements SecuredObject {

    @Id
    private Long id;
    private String property;

    public TestObject() {
    }

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
