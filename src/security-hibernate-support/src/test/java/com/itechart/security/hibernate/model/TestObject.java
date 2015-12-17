package com.itechart.security.hibernate.model;

import com.itechart.security.core.model.acl.SecuredObject;

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

    public TestObject() {
    }

    public TestObject(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getObjectType() {
        return "TestObject";
    }
}
